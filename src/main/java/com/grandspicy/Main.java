package com.grandspicy;

import com.grandspicy.dao.BaseDatos;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import java.io.File;

// Punto de entrada para desarrollo con Tomcat embebido.
// Crea la BD, lanza Tomcat y sirve la web en localhost:8080
public class Main {

    public static void main(String[] args) throws Exception {
        BaseDatos.inicializar();

        System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");

        String puerto = System.getenv("PORT");
        if (puerto == null) puerto = "8080";

        String rutaBase = new File("target/tomcat").getAbsolutePath();
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(rutaBase);
        tomcat.setPort(Integer.parseInt(puerto));

        String rutaWebapp = new File("src/main/webapp").getAbsolutePath();
        if (!new File(rutaWebapp).exists()) {
            rutaWebapp = new File("webapp").getAbsolutePath();
        }
        if (!new File(rutaWebapp).exists()) {
            rutaWebapp = Main.class.getResource("/webapp") != null
                ? Main.class.getResource("/webapp").getPath()
                : null;
        }

        Context ctx = tomcat.addWebapp("", rutaWebapp);
        ctx.setParentClassLoader(Main.class.getClassLoader());

        File clasesCompiladas = new File("target/classes");
        if (clasesCompiladas.exists()) {
            WebResourceRoot recursos = new StandardRoot(ctx);
            recursos.addPreResources(
                new DirResourceSet(recursos, "/WEB-INF/classes",
                    clasesCompiladas.getAbsolutePath(), "/"));
            ctx.setResources(recursos);
        }

        tomcat.getConnector();
        tomcat.start();

        System.out.println();
        System.out.println("======================================");
        System.out.println("  GrandSpicy ready!");
        System.out.println("  Open: http://localhost:" + puerto);
        System.out.println("======================================");
        System.out.println();

        tomcat.getServer().await();
    }
}
