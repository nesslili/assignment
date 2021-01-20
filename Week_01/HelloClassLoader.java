package jvm;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Base64;
public class HelloClassLoader extends ClassLoader {


    public static void main(String[] args) throws Exception {
        HelloClassLoader myc = new HelloClassLoader();
        Class<?> c = myc.loadClass("Hello.xlass");//字符串是要加载类的 package
        Object obj = c.newInstance();
        System.out.println(obj);
        System.out.println(obj.getClass().getClassLoader());
        System.out.println(obj.getClass().getClassLoader().getParent());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> c = null;
        byte[] data = new byte[0];
        try{
            data = getClassData(name);
            c = defineClass(name,data,0,data.length-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    private byte[] getClassData(String classname) throws IOException {
        String path = "c:/Users/lzheng/JAVA-01/Week_01/";
        String classData = path + classname;
        FileInputStream fis = new FileInputStream(classData);
        FileChannel fileC = fis.getChannel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel outC = Channels.newChannel(baos);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while(true) {
            int i = fileC.read(buffer);
            if (i == 0 || i == -1) {
                break;
            }
            buffer.flip();
            outC.write(buffer);
            buffer.clear();
        }
        fis.close();
        return baos.toByteArray();

    }
}