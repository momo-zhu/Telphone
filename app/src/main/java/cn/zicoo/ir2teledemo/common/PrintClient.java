package cn.zicoo.ir2teledemo.common;

import android.content.Context;

import com.gprinter.command.EscCommand;

import static com.gprinter.command.EscCommand.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.kymjs.kjframe.ui.ViewInject;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import cn.zicoo.ir2teledemo.R;
import cn.zicoo.ir2teledemo.model.Customer;
import cn.zicoo.ir2teledemo.model.Order;
import cn.zicoo.ir2teledemo.model.OrderItem;

public class PrintClient {
    public static Socket socket = null;

    public static synchronized boolean Connect(String ip, int port) {
        try {

            socket = new Socket();
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(10000);
            socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        //return OpenPort(portType,ip,port);
    }

    public static boolean ReConnect(String ip, int port) {
        try {
            close();
            socket = new Socket();
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(10000);
            socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //return OpenPort(portType,ip,port);
    }

    public static boolean close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static void SendData(byte[] data) {
        new Thread(() ->
        {
            if (socket == null || !socket.isConnected()) {
                boolean result = Connect(Global.setting.PrinterIp, Global.setting.PrinterPort);
                if (!result) {
                    ViewInject.toast(R.string.connect_printer_failed);
                    return;
                }
            }
            if (data != null && data.length > 0) {
                try {
                    OutputStream out = socket.getOutputStream();
                    out.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }

    public static void Test() {
        EscCommand esc = new EscCommand();
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, ENABLE.OFF, ENABLE.OFF);// 倍高
        esc.addSelectJustification(JUSTIFICATION.CENTER);// 设置打印左对齐
        esc.addPrintAndLineFeed();
        esc.addText("Test\n"); // 打印文字

        esc.addPrintAndLineFeed();
        esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF);// 倍高

        esc.addSelectJustification(JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("TestTestTestTestTest"); // 打印文字

        esc.addPrintAndLineFeed();

        esc.addText("hello"); // 打印文字
        //esc.addSetHorAndVerMotionUnits(7, 0);

        esc.addSelectJustification(JUSTIFICATION.CENTER);// 设置打印左对齐
        esc.addText("end\n"); // 打印文字

        esc.addCutPaper();
        Byte[] data = esc.getCommand().toArray(new Byte[0]);
        SendData(ArrayUtils.toPrimitive(data));
    }

    public static void Print(Order order, Customer currentCustomer, Context cxt) {
        EscCommand esc = new EscCommand();
        esc.addSelectPrintModes(FONT.FONTA, ENABLE.ON, ENABLE.ON, ENABLE.ON, ENABLE.OFF);// 倍高
        esc.addSelectJustification(JUSTIFICATION.CENTER);// 设置打印左对齐
        if (StringUtils.isNotBlank(Global.setting.ShopName))
            esc.addText(Global.setting.ShopName); // 打印文字
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF);// 倍高
        esc.addText(cxt.getString(R.string.customer_label) + currentCustomer.Name + "（" + currentCustomer.Telephone + "）"); // 打印文字
        esc.addPrintAndLineFeed();
        esc.addText("----------------------------"); // 打印文字
        esc.addPrintAndLineFeed();
        esc.addSelectJustification(JUSTIFICATION.LEFT);// 设置打印左对齐

        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 7);
        for (OrderItem item : order.Items) {
            esc.addSetAbsolutePrintPosition((byte) 2);

            esc.addText(item.Name);
            if (StringUtils.isNotBlank(item.Properties))
                esc.addText("(" + item.Properties + ")");
            esc.addSetAbsolutePrintPosition((short) 15);
            if (item.Quantity > 1)
                esc.addText(String.format("%.2f x %d", item.Price, item.Quantity));
            else
                esc.addText(String.format("%.2f", item.Price));

            esc.addPrintAndLineFeed();

        }

        esc.addSetAbsolutePrintPosition((short) 2);

        if (StringUtils.isNotBlank(order.Remark))
            esc.addText(cxt.getString(R.string.remark_label) + order.Remark);
        esc.addPrintAndLineFeed();

        esc.addSetAbsolutePrintPosition((short) 2);

        esc.addText(cxt.getString(R.string.table_no));
        esc.addSetAbsolutePrintPosition((short) 15);
        if (order.TableId != 0)
            esc.addText(String.valueOf(order.TableId));
        esc.addPrintAndLineFeed();

        esc.addSelectJustification(JUSTIFICATION.CENTER);
        esc.addText("--------------------------"); // 打印文字
        esc.addPrintAndLineFeed();

        esc.addSetAbsolutePrintPosition((short) 2);

        esc.addText(cxt.getString(R.string.order_subtotal));
        esc.addSetAbsolutePrintPosition((short) 15);
        esc.addText(String.format("%.2f", order.SubTotal));
        esc.addPrintAndLineFeed();
        esc.addSetAbsolutePrintPosition((short) 2);

        esc.addText(cxt.getString(R.string.order_taxes));
        esc.addSetAbsolutePrintPosition((short) 15);
        esc.addText(String.format("%.2f", order.TaxTotal));

        esc.addPrintAndLineFeed();
        esc.addSetAbsolutePrintPosition((short) 2);

        esc.addText(cxt.getString(R.string.order_tips));
        esc.addSetAbsolutePrintPosition((short) 15);
        esc.addText(String.format("%.2f", order.TipsTotal));

        esc.addPrintAndLineFeed();

        esc.addSetAbsolutePrintPosition((short) 2);
        esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.ON, ENABLE.ON, ENABLE.OFF);// 倍高

        esc.addText(cxt.getString(R.string.order_total));
        esc.addSetAbsolutePrintPosition((short) 13);
        esc.addText(String.format("%.2f", order.TipsTotal + order.SubTotal + order.TaxTotal));

        esc.addPrintAndLineFeed();
        esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF);// 倍高

        esc.addSelectJustification(JUSTIFICATION.CENTER);
        esc.addText("--------------------------"); // 打印文字
        esc.addPrintAndLineFeed();
        esc.addSetAbsolutePrintPosition((short) 2);
        esc.addText(cxt.getString(R.string.print_time)); // 打印文字
        esc.addSetAbsolutePrintPosition((short) 15);
        esc.addText(DateUtils.formatDate(order.CreateDate, "HH:mm")); // 打印文字
        esc.addPrintAndLineFeed();

        esc.addSetAbsolutePrintPosition((short) 2);
        esc.addText(cxt.getString(R.string.dinner_time)); // 打印文字
        esc.addSetAbsolutePrintPosition((short) 15);
        esc.addText(DateUtils.formatDate(order.DeliveryDate, "HH:mm")); // 打印文字

        esc.addPrintAndFeedPaper((byte) 3);
        esc.addPrintAndFeedLines((byte) 3);

        esc.addCutAndFeedPaper((byte) 3);
        Byte[] data = esc.getCommand().toArray(new Byte[0]);
        SendData(ArrayUtils.toPrimitive(data));
    }


    public static void CheckPrinter(int[] localIp, int port, List<String> printers, ExecutorService es) {
        for (int i = 0; i <= 255; i++) {
            if (i != localIp[3]) {
                String remoteIp = localIp[0] + "." + localIp[1] + "." + localIp[2] + "." + i;
                Runnable task = () -> {
                    try {
                        Socket client = new Socket();
                        client.setTcpNoDelay(true);
                        client.setSoTimeout(300);
                        client.connect(new InetSocketAddress(InetAddress.getByName(remoteIp), port));
                        if (client.isConnected()) {
                            printers.add(remoteIp);
                            client.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                es.submit(task);
            }
        }
        try {
            es.shutdown();
            es.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
