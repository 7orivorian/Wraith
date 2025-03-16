package dev.tori.wraith.subscriber;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.bus.IEventBus;
import dev.tori.wraith.event.Target;
import dev.tori.wraith.listener.annotation.Listener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 4.1.0
 */
public class AnnotatedSubscriberTest {

    private final EventBus bus = new EventBus();

    private static class PrivateTestParent {
        private final AnnotatedSubscriber subscriber = new AnnotatedSubscriber(this);
        private final IEventBus eventBus;

        private PrivateTestParent(final IEventBus eventBus) {
            this.eventBus = eventBus;
            eventBus.subscribe(subscriber);
        }

        public void unsubscribe() {
            eventBus.unsubscribe(subscriber);
        }
    }

    /* Method Modifiers Tests */

    @Test
    void methodModifierTests() {
        MethodModifierTests methodModifierTests = new MethodModifierTests(bus);

        bus.dispatch(true);
        Assertions.assertTrue(methodModifierTests.isPublicTest());
        bus.dispatch(1);
        Assertions.assertTrue(methodModifierTests.isPrivateTest());
        bus.dispatch(1.2D);
        Assertions.assertTrue(methodModifierTests.isProtectedTest());
        bus.dispatch(1.3f);
        Assertions.assertTrue(methodModifierTests.isPackagePrivateTest());
    }

    public static class MethodModifierTests extends PrivateTestParent {

        private boolean publicTest = false;
        private boolean privateTest = false;
        private boolean protectedTest = false;
        private boolean packagePrivateTest = false;

        public MethodModifierTests(IEventBus eventBus) {
            super(eventBus);
        }

        @Listener
        public void publicTest(boolean worked) {
            publicTest = worked;
        }

        @Listener
        private void privateTest(int event) {
            privateTest = true;
        }

        @Listener
        protected void protectedTest(double event) {
            protectedTest = true;
        }

        @Listener
        void packagePrivateTest(float event) {
            packagePrivateTest = true;
        }

        public boolean isPublicTest() {
            return publicTest;
        }

        public boolean isPrivateTest() {
            return privateTest;
        }

        public boolean isProtectedTest() {
            return protectedTest;
        }

        public boolean isPackagePrivateTest() {
            return packagePrivateTest;
        }
    }

    /* End of Method Primitive Tests */

    /* Primitive Tests */

    /**
     * Looking if the {@linkplain AnnotatedSubscriber} will wrap the correct primitives so it will work with auto-boxing.
     */
    @Test
    void primitiveParametersTest() {
        PrimitiveTest test = new PrimitiveTest(bus);

        bus.dispatch(1);
        Assertions.assertEquals(1, test.getIntPrimitive());
        bus.dispatch((short) 100);
        Assertions.assertEquals(100, test.getShortPrimitive());
        bus.dispatch(1.5f);
        Assertions.assertEquals(1.5f, test.getFloatPrimitive());
        bus.dispatch(2.5D);
        Assertions.assertEquals(2.5D, test.getDoublePrimitive());
        bus.dispatch(true);
        Assertions.assertTrue(test.isBooleanPrimitive());
        bus.dispatch('s');
        Assertions.assertEquals('s', test.getCharPrimitive());
        bus.dispatch((byte) 0x01);
        Assertions.assertEquals(0x01, test.getBytePrimitive());
        test.unsubscribe();
    }

    public static class PrimitiveTest extends PrivateTestParent {

        private int intPrimitive;
        private short shortPrimitive;
        private float floatPrimitive;
        private double doublePrimitive;
        private boolean booleanPrimitive;
        private char charPrimitive;
        private byte bytePrimitive;

        public PrimitiveTest(IEventBus eventBus) {
            super(eventBus);
        }

        @Listener
        public void integerTest(int event) {
            intPrimitive = event;
        }

        @Listener
        public void shortTest(short event) {
            shortPrimitive = event;
        }

        @Listener
        public void floatTest(float event) {
            floatPrimitive = event;
        }

        @Listener
        public void doubleTest(double event) {
            doublePrimitive = event;
        }

        @Listener
        public void booleanTest(boolean event) {
            booleanPrimitive = event;
        }


        @Listener
        public void charTest(char event) {
            charPrimitive = event;
        }

        @Listener
        public void byteTest(byte event) {
            bytePrimitive = event;
        }

        public int getIntPrimitive() {
            return intPrimitive;
        }

        public short getShortPrimitive() {
            return shortPrimitive;
        }

        public float getFloatPrimitive() {
            return floatPrimitive;
        }

        public double getDoublePrimitive() {
            return doublePrimitive;
        }

        public boolean isBooleanPrimitive() {
            return booleanPrimitive;
        }

        public char getCharPrimitive() {
            return charPrimitive;
        }

        public byte getBytePrimitive() {
            return bytePrimitive;
        }
    }

    /* End Primitive */

    // God help us all.

    /* Targeting Tests */

    @Test
    void targetingFineTest() {
        FineTargetTest test = new FineTargetTest(bus);

        EventA eventA = new EventA();
        EventB eventB = new EventB();
        EventC eventC = new EventC();
        bus.dispatch(eventA);
        bus.dispatch(eventB);
        bus.dispatch(eventC);

        Assertions.assertEquals(1, eventA.mods);
        Assertions.assertEquals(0, eventB.mods);
        Assertions.assertEquals(0, eventC.mods);
        test.unsubscribe();
    }

    @Test
    void targetingCascadeTest() {
        CascadeTargetTest test = new CascadeTargetTest(bus);

        ParentEvent event = new ParentEvent();
        bus.dispatch(event);
        Assertions.assertEquals(1, event.mods);

        ParentEvent.ChildEvent childEvent = new ParentEvent.ChildEvent();
        bus.dispatch(childEvent);
        Assertions.assertEquals(2, childEvent.mods);
        test.unsubscribe();
    }

    @Test
    void targetingReverseCascadeTest() {
        ReverseCascadeTargetTest test = new ReverseCascadeTargetTest(bus);

        ParentEvent event = new ParentEvent();
        bus.dispatch(event);
        Assertions.assertEquals(1, event.mods);

        ParentEvent.ChildEvent childEvent = new ParentEvent.ChildEvent();
        bus.dispatch(childEvent);
        Assertions.assertEquals(2, childEvent.mods);
        test.unsubscribe();
    }

    public static class FineTargetTest extends PrivateTestParent {

        public FineTargetTest(IEventBus eventBus) {
            super(eventBus);
        }

        @Listener
        private void fineTarget(EventA eventA) {
            eventA.mod();
        }
    }

    public static class CascadeTargetTest extends PrivateTestParent {

        public CascadeTargetTest(IEventBus eventBus) {
            super(eventBus);
        }

        @Listener(rule = Target.TargetingRule.CASCADE)
        public void cascadeTarget(ParentEvent parentEvent) {
            parentEvent.mod();
        }
    }

    public static class ReverseCascadeTargetTest extends PrivateTestParent {

        public ReverseCascadeTargetTest(IEventBus eventBus) {
            super(eventBus);
        }

        @Listener(rule = Target.TargetingRule.REVERSE_CASCADE, target = ParentEvent.ChildEvent.class)
        public void reverseCascadeTarget(ParentEvent parentEvent) {
            parentEvent.mod();
        }
    }

    public static class EventA {

        public int mods = 0;

        public void mod() {
            this.mods++;
        }
    }

    public static class EventB {

        public int mods = 0;

        public void mod() {
            this.mods++;
        }
    }

    public static class EventC {

        public int mods = 0;

        public void mod() {
            this.mods++;
        }
    }

    public static class ParentEvent {

        public int mods = 0;

        public void mod() {
            this.mods++;
        }

        public static class ChildEvent extends ParentEvent {

            @Override
            public void mod() {
                this.mods += 2;
            }
        }
    }
    /* End of Targeting Test */
}