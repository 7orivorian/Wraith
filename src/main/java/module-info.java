/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.1.0
 */
module dev.tori.wraith {
    requires transitive org.jetbrains.annotations;

    exports dev.tori.wraith.bus;
    exports dev.tori.wraith.event;
    exports dev.tori.wraith.event.status;
    exports dev.tori.wraith.listener;
    exports dev.tori.wraith.subscriber;
    exports dev.tori.wraith.task;
    exports dev.tori.wraith.util;
}