/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kymjs.kjframe.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.SupportActivity;

import java.lang.reflect.Field;

/**
 * 注解工具类<br>
 * <p/>
 * <b>创建时间</b> 2014-6-5
 *
 * @author kymjs (https://github.com/kymjs)
 * @version 1.1
 */
public class AnnotateUtil {

    private static Class[] filters = {KJActivity.class, KJFragment.class, SupportFragment.class, SupportActivity.class, Object.class};

    /**
     * @param currentClass 当前类，一般为Activity或Fragment
     * @param sourceView   待绑定控件的直接或间接父控件
     */
    public static void initBindView(Object currentClass, Class<?> clazz, View sourceView) {
        // 通过反射获取到全部属性，反射的字段可能是一个类（静态）字段或实例字段
        Class superClass = clazz.getSuperclass();
        boolean bindSuperClass = true;
        for (Class filter : filters)
            if (superClass == filter) {
                bindSuperClass = false;
                break;
            }
        if (bindSuperClass)
            initBindView(currentClass, superClass, sourceView);
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                // 返回BindView类型的注解内容
                BindView bindView = field.getAnnotation(BindView.class);
                if (bindView != null) {
                    int viewId = bindView.id();
                    boolean clickLis = bindView.click();
                    try {
                        field.setAccessible(true);
                        // 将currentClass的field赋值为sourceView.findViewById(viewId)
                        View view = sourceView.findViewById(viewId);
                        if (view != null) {
                            field.set(currentClass, view);
                            if (clickLis) {
                                view.setOnClickListener((OnClickListener) currentClass);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 必须在setContentView之后调用
     *
     * @param aty Activity对象
     */
    public static void initBindView(Activity aty) {
        initBindView(aty, aty.getClass(), aty.getWindow().getDecorView());
    }

    /**
     * 必须在setContentView之后调用
     *
     * @param view 侵入式的view，例如使用inflater载入的view
     */
    public static void initBindView(View view) {
        Context cxt = view.getContext();
        if (cxt instanceof Activity) {
            initBindView((Activity) cxt);
        } else {
            throw new RuntimeException("view must into Activity");
        }
    }

    /**
     * 必须在setContentView之后调用
     *
     * @param frag 要初始化的Fragment
     */
    public static void initBindView(Fragment frag) {
        initBindView(frag, frag.getClass(), frag.getActivity().getWindow().getDecorView());
    }
}
