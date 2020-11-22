module work.fking.pangya.login {
    requires org.apache.logging.log4j;
    requires fking.work.pangya.networking;
    requires com.google.guice;
    requires io.netty.transport;
    requires work.fking.pangya.common;
    requires io.netty.common;
    requires com.fasterxml.jackson.databind;
    requires com.zaxxer.hikari;
    requires org.jdbi.v3.core;
    requires org.jdbi.v3.jackson2;
    requires org.jdbi.v3.postgres;
    requires org.jdbi.v3.sqlobject;
    requires javax.inject;
    requires java.sql;
    requires org.jdbi.v3.json;
    requires bcrypt;
    requires io.netty.buffer;
}