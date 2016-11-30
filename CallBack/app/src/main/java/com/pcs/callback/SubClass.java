package com.pcs.callback;

/**
 * Created by pcs20 on 10/2/15.
 */
public class SubClass {

    interface MyCallbackClass {
        void callbackReturn();
    }

    MyCallbackClass myCallbackClass;

    void registerCallback(MyCallbackClass callbackClass) {
        myCallbackClass = callbackClass;
    }

    void doSomething() {
        MainActivity.fromCallBack = true;
        myCallbackClass.callbackReturn();
    }
}
