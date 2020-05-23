package com.github.corviv;

import org.nmap4j.core.flags.Flag;
import org.nmap4j.core.nmap.ExecutionResults;
import org.nmap4j.core.scans.BaseScan;
import org.nmap4j.core.scans.IScan;
import org.nmap4j.data.NMapRun;
import org.nmap4j.parser.OnePassParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;

import java.util.stream.IntStream;

@Listeners(ListenerLogger.class)
public class INmap4j {
    static final String pathNmap = "C:\\Program Files (x86)\\Nmap\\";
    private static BaseScan scan = null;
    private static ExecutionResults output = null;
    static NMapRun results = null;
    private static IScan.TimingFlag timingFlag = IScan.TimingFlag.AGGRESSIVE;

    private static final Logger logger = LoggerFactory.getLogger("INmap4j");

    public INmap4j() {
        scan = new BaseScan(pathNmap);
    }

    public interface portState {
        String OPEN = "open";
        String CLOSED = "closed";
        String FILTERED = "filtered";
    }

    public static void addDstHosts(String[] hosts) {
        scan.includeHosts(hosts);
    }

    public static void addDstPorts(int port) {
        scan.addPort(port);
    }

    public static void addDstPorts(int[] ports) {
        scan.addPorts(ports);
    }

    public static void addSrcPort(int port) {
        scan.addFlag(Flag.SOURCE_PORT, String.valueOf(port));
    }

    public static void addFlag(Flag flag) {
        scan.addFlag(flag);
    }

    public static void addFlag(Flag flag, String value) {
        scan.addFlag(flag, value);
    }

    public static void setTiming(IScan.TimingFlag flag) {
        timingFlag = flag;
    }

    public static void scan() {

        scan.setTiming(timingFlag);

        try {
            output = scan.executeScan();
            if (output.hasErrors()) {
                logger.error("Nmap: error in results scan!");
                logger.info("Executed command: " + output.getExecutedCommand() + "\n");
            }
            logger.debug("Nmap debug output:\n" + output.getOutput());
            OnePassParser opp = new OnePassParser();
            results = opp.parse(output.getOutput(), OnePassParser.STRING_INPUT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        output = null;
        scan = null;
    }

    public static void scanTCP(int srcPort, String[] dstHosts) {
        scanTCP(null, null, srcPort, null, dstHosts);
    }

    public static void scanTCP(int[] dstPorts, String[] dstHosts) {
        scanTCP(null, null, 0, dstPorts, dstHosts);
    }

    public static void scanTCP(int srcPort, int[] dstPorts, String[] dstHosts) { scanTCP(null, null, srcPort, dstPorts, dstHosts); }

    public static void scanTCP(String spoofSrcAddress, String inet, int srcPort, String[] dstHosts){ scanTCP(spoofSrcAddress, inet, srcPort, null, dstHosts);}

    public static void scanTCP(String spoofSrcAddress, String inet, int[] dstPorts, String[] dstHosts){ scanTCP(spoofSrcAddress, inet, 0, dstPorts, dstHosts);}

    public static void scanTCP(String spoofSrcAddress, String inet, int srcPort, int[] dstPorts, String[] dstHosts) {
        if (spoofSrcAddress != null){
            addFlag(Flag.INTERFACE, inet);
            addFlag(Flag.SPOOF_SOURCE_ADDRESS, spoofSrcAddress);
        }
        if (srcPort != 0){
            addSrcPort(srcPort);
        }
        addDstHosts(dstHosts);
        if (dstPorts != null){
            addDstPorts(dstPorts);
        }
        addFlag(Flag.TCP_SYN_SCAN);
        scan();
    }

    public static void sendUDP(int srcPort, String[] dstHosts) {
        sendUDP(srcPort, null, dstHosts);
    }

    public static void sendUDP(int[] dstPorts, String[] dstHosts) {
        sendUDP(0, dstPorts, dstHosts);
    }

    public static void sendUDP(int srcPort, int[] dstPorts, String[] dstHosts) {
        if (srcPort != 0)
            addSrcPort(srcPort);
        addDstHosts(dstHosts);
        if (dstPorts != null)
            addDstPorts(dstPorts);
        addFlag(Flag.UDP_SCAN);
        scan();
    }

    public static int getScannedPortsCount() {
        return results.getHosts().get(0).getPorts().getPorts().size();
    }

    public static String getPortState(int index) {
        return results.getHosts().get(0).getPorts().getPorts().get(index).getState().getState();
    }

    public static long getPortNumber(int index) {
        return results.getHosts().get(0).getPorts().getPorts().get(index).getPortId();
    }

    public static int[] setRange (int startPort, int endPort){
        int[] rangePorts = IntStream.rangeClosed(startPort,endPort).toArray();
        return rangePorts;
    }
}
