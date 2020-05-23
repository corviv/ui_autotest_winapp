package com.github.corviv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.net.*;

public class UDPUtils {

    @Listeners(ListenerLogger.class)
    public static class EchoServerUDP extends Thread {

        public enum Mode {
            CHECK_CONDITIONS,
            PROMISCUOUS,
            ECHO
        }

        public enum State {
            IDLE,
            RUNNING,
            CONDITIONS_ARE_MET,
            TIMEOUT
        }

        private DatagramSocket socket;
        private byte[] buf = new byte[256];
        private String datagram = null;
        private int bindPort = 0;
        private int rcvPort = 0;
        private String rcvAddress = null;
        private String srcAddress = null;
        private long rcvTimeoutMs = 0;
        private String specMsg = null;
        private int srcPort = 0;

        private Mode currentMode  = Mode.CHECK_CONDITIONS;
        private State currentState = State.IDLE;

        private final Logger logger = LoggerFactory.getLogger("EchoServerUDP");

        public EchoServerUDP(int bindPort, String srcAddress) throws SocketException {
            this.bindPort = bindPort;
            this.srcAddress = srcAddress;
            socket = new DatagramSocket(bindPort);
        }

        public State getCurrentState() {
            return currentState;
        }
        public boolean isConditionsMet() {
            return currentState == State.CONDITIONS_ARE_MET;
        }

        public void enablePromiscuousMode() {
            currentMode = Mode.PROMISCUOUS;
        }
        public void enableEcho() {
            currentMode = Mode.ECHO;
        }

        public void setSrcPort(int srcPort) {
            this.srcPort = srcPort;
        }

        public void setSpecMsg(String specMsg) {
            this.specMsg = specMsg;
        }

        public void setTimeout(int rcvTimeoutMs) throws SocketException {
            this.rcvTimeoutMs = rcvTimeoutMs;
            socket.setSoTimeout(rcvTimeoutMs);
        }

        public boolean isTimeout() {
            return currentState == State.TIMEOUT;
        }

        private boolean checkConditions(DatagramPacket packet) {

            if (!srcAddress.equals(rcvAddress)) {
                logger.info("Received package from " + rcvAddress);
                logger.warn("Conditions weren't met!");
                return false;
            }
            if (srcPort != 0) {
                if (srcPort != rcvPort) {
                    logger.info("Received package from " + rcvAddress + ":" + rcvPort);
                    logger.warn("Conditions weren't met!");
                    return false;
                }
            }
            if (specMsg != null) {
                if (!datagram.equals(specMsg)) {
                    logger.info("Received package from " + rcvAddress + ":" + rcvPort + " with message '" + datagram + "'");
                    logger.warn("Conditions weren't met!");
                    return false;
                }
            }
            logger.info("Received package from " + rcvAddress + ":" + rcvPort + " with message '" + datagram + "'");
            logger.info("Conditions have been met successfully!");
            currentState = State.IDLE;
            return true;
        }

        private void sendEcho(DatagramPacket packet) throws IOException {
            InetAddress address = packet.getAddress();
            packet = new DatagramPacket(buf, buf.length, address, packet.getPort());
            socket.send(packet);
            logger.info("Echo packet was sent..");
        }

        public void run() {
            currentState = State.RUNNING;
            logger.info("Listen port " + bindPort + "....");
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                while (true) {
                    socket.receive(packet);
                    rcvAddress = packet.getAddress().getHostAddress();
                    rcvPort = packet.getPort();
                    datagram = new String(packet.getData(), 0, packet.getLength());

                    switch (currentMode) {
                        case CHECK_CONDITIONS:
                            if (checkConditions(packet))
                                currentState = State.CONDITIONS_ARE_MET;
                            break;
                        case PROMISCUOUS:
                            logger.info("Received packet from " + rcvAddress + ":" + rcvPort + " with message '" + datagram + "'");
                            continue;
                        case ECHO:
                            logger.info("Received packet from " + rcvAddress + ":" + rcvPort + " with message '" + datagram + "'");
                            sendEcho(packet);
                            continue;
                    }
                    break;
                }
            } catch (SocketTimeoutException e) {
                currentState = State.TIMEOUT;
                logger.info("Timeout!");
                logger.info("The packet was not received during the specified timeout: " + rcvTimeoutMs + " ms");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        }

        public void close() {
            currentState = State.IDLE;
            socket.close();
        }
    }

    @Listeners(ListenerLogger.class)
    public static class ClientUDP {

        private DatagramSocket socket;
        private InetAddress address;
        private int port;
        private int sendPeriodMs = 1000;
        private String specMsg = "";
        private final Logger logger = LoggerFactory.getLogger("ClientUDP");

        public enum Mode {
            SINGLE,
            CONTINUOUS
        }

        public enum State {
            IDLE,
            RUNNING
        }

        private Mode currentMode  = Mode.SINGLE;
        private State currentState = State.IDLE;

        public ClientUDP(String dstAddress, int dstPort) throws SocketException, UnknownHostException {
            socket = new DatagramSocket();
            port = dstPort;
            address = InetAddress.getByName(dstAddress);
        }

        public void enableContinuousMode(int sendPeriodMs) {
            currentMode = Mode.CONTINUOUS;
            this.sendPeriodMs = sendPeriodMs;
        }

        public State getCurrentState() {
            return currentState;
        }

        public void setPeriod(int sendPeriodMs) {
            this.sendPeriodMs = sendPeriodMs;
        }

        public void setSpecMsg(String Msg) {
            specMsg = Msg;
        }

        public void send() throws IOException {
            byte[] buf = specMsg.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
            logger.info("Packet with message '" + specMsg + "' sent to " + address + ":" + port);
        }

        public void run() {
            try {
                currentState = State.RUNNING;
                while(true) {

                    switch(currentMode) {
                        case SINGLE:
                            send();
                            break;
                        case CONTINUOUS:
                            send();
                            Thread.sleep(sendPeriodMs);
                            continue;
                    }
                    break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                currentState = State.IDLE;
            }
        }

        public void close() {
            currentState = State.IDLE;
            socket.close();
        }
    }
}
