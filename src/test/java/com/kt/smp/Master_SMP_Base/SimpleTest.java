package com.kt.smp.Master_SMP_Base;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class SimpleTest {

    @Test
    void test() {

        String url = "http://125.159.61.195:55243";
        URI uri = URI.create(url);
        System.out.println(uri.getScheme());
        System.out.println(uri.getHost());
        System.out.println(uri.getPort());

    }

    @Test
    void write() {

        Path path = Paths.get("/Users/minwoo/Desktop/answerSheet.txt");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            List<String> dataList = Arrays.asList("test1", "test2", "test3");

            for (String data : dataList) {
                writer.write(data);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
