//package com.gent00.datapoint.generator;
//
//import picocli.CommandLine;
//
//import java.io.File;
//import java.io.PrintStream;
//import java.io.PrintWriter;
//import java.util.List;
//
//public class ScenarioCommander implements Runnable {
//
//    PrintStream info = System.err;
//
//    @CommandLine.Command(mixinStandardHelpOptions = true, version = "auto help demo - picocli 3.0")
//
//    @CommandLine.Option(names = "-b", description = "Block Sizes", split = ",")
//    List<String> blockSizes;
//
//    @CommandLine.Option(names = "-a", description = "Use libaio")
//    boolean async = false;
//
//
//    @CommandLine.Option(names = {"-f", "--file"}, paramLabel = "ARCHIVE", description = "the archive file")
//    File[] files;
//
//    public static void main(String argsv[]) {
//        String[] args = {
//                "-b", "4k,10k",
//                "-b", "8k",
//                "-f", "/dev/nvm0",
//                "-f", "/dev/nvm1",
////                "--help"
//        };
//        CommandLine.run(new ScenarioCommander(), args);
//        System.exit(-1);
//    }
//
//    @Override
//    public void run() {
//        ScenarioGenerator generator = new ScenarioGenerator(ScenarioGenerator.Scenario.MULTIPLE_DRIVE,blockSizes,files);
//        try {
//            generator.execute();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}
