package com.example.mytestapp.activity;

import com.example.mytestapp.entity.BaseItemEntity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.List;

import timber.log.Timber;

public class WebSocketActivity extends BaseListActivity {


    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("开始连接", "0"));
        datas.add(new BaseItemEntity("关闭连接", "1"));
        datas.add(new BaseItemEntity("onClick2", "2"));
        datas.add(new BaseItemEntity("onClick3", "3"));
        datas.add(new BaseItemEntity("onClick4", "4"));
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                click0();
                break;

            case 1:
                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;
        }
    }


    private void click0() {
        String host = "https://push.qcg.cc";
        String path = "/push/ws/";
        String query = "device_id=bf073d7156db4a75d35f96eaaa17e460&gnw_appid=437EC0AC7F0000015E2BBF4849643C96&version=7.2.0&channel=01&sign=1617159170295a8195ed2fd3b3dd36fdcb61f23bd82ebf1c69ab6e2cdc7d7987aedc609251ad7";
        URI serverURI = URI.create("wss://push.qcg.cc/push/ws?device_id=bf073d7156db4a75d35f96eaaa17e460&gnw_appid=437EC0AC7F0000015E2BBF4849643C96&version=7.2.0&channel=01&sign=1617159170295a8195ed2fd3b3dd36fdcb61f23bd82ebf1c69ab6e2cdc7d7987aedc609251ad7");

        WebSocketClient webSocketClient = new WebSocketClient(serverURI) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Timber.d("onOpen");
            }

            @Override
            public void onMessage(String message) {
                Timber.d("onMessage = " + message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Timber.d("onClose");
            }

            @Override
            public void onError(Exception ex) {
                Timber.d("onError");
            }
        };
        webSocketClient.connect();

//        webSocketClient.send("device_id=bf073d7156db4a75d35f96eaaa17e460&key=123");
    }

    private void click1() {
        URI serverURI = URI.create("https://push.qcg.cc/push/ws?device_id=bf073d7156db4a75d35f96eaaa17e460&gnw_appid=437EC0AC7F0000015E2BBF4849643C96&version=7.2.0&channel=01&sign=1617159170295a8195ed2fd3b3dd36fdcb61f23bd82ebf1c69ab6e2cdc7d7987aedc609251ad7");

        WebSocketClient webSocketClient = new WebSocketClient(serverURI) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Timber.d("onOpen");
            }

            @Override
            public void onMessage(String message) {
                Timber.d("onMessage = " + message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Timber.d("onClose");
            }

            @Override
            public void onError(Exception ex) {
                Timber.d("onError");
            }
        };

        webSocketClient.connect();
//        webSocketClient.send("device_id=bf073d7156db4a75d35f96eaaa17e460&key=456");
    }

    private void click2() {

    }

    private void click3() {

    }
}