/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.1.0
 */
module dev.tori.wraith.test {
    requires dev.tori.wraith;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;
    requires org.junit.jupiter.engine;
    requires org.junit.platform.engine;
    requires org.junit.platform.commons;
    requires jmh.core;
    requires org.jetbrains.annotations;

    exports dev.tori.wraith.test;

    opens dev.tori.wraith.test to org.junit.platform.commons;
}