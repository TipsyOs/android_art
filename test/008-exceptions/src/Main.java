/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// An error class.
class BadError extends Error {
    public BadError(String s) {
        super("This is bad by convention: " + s);
    }
}

// A class that throws BadError during static initialization.
class BadInit {
    static int dummy;
    static {
        System.out.println("Static Init");
        if (true) {
            throw new BadError("BadInit");
        }
    }
}

// An error that doesn't have a <init>(String) method.
class BadErrorNoStringInit extends Error {
    public BadErrorNoStringInit() {
        super("This is bad by convention");
    }
}

// A class that throws BadErrorNoStringInit during static initialization.
class BadInitNoStringInit {
    static int dummy;
    static {
        System.out.println("Static BadInitNoStringInit");
        if (true) {
            throw new BadErrorNoStringInit();
        }
    }
}

/**
 * Exceptions across method calls
 */
public class Main {
    public static void exceptions_007() {
        try {
            catchAndRethrow();
        } catch (NullPointerException npe) {
            System.out.print("Got an NPE: ");
            System.out.println(npe.getMessage());
            npe.printStackTrace(System.out);
        }
    }
    public static void main (String args[]) {
        exceptions_007();
        exceptionsRethrowClassInitFailure();
        exceptionsRethrowClassInitFailureNoStringInit();
    }

    private static void catchAndRethrow() {
        try {
            throwNullPointerException();
        } catch (NullPointerException npe) {
            NullPointerException npe2;
            npe2 = new NullPointerException("second throw");
            npe2.initCause(npe);
            throw npe2;
        }
    }

    private static void throwNullPointerException() {
        throw new NullPointerException("first throw");
    }

    private static void exceptionsRethrowClassInitFailure() {
        try {
            try {
                BadInit.dummy = 1;
                throw new IllegalStateException("Should not reach here.");
            } catch (BadError e) {
                System.out.println(e);
            }

            // Check if it works a second time.

            try {
                BadInit.dummy = 1;
                throw new IllegalStateException("Should not reach here.");
            } catch (NoClassDefFoundError e) {
                System.out.println(e);
                System.out.println(e.getCause());
            }
        } catch (Exception error) {
            error.printStackTrace(System.out);
        }
    }

    private static void exceptionsRethrowClassInitFailureNoStringInit() {
        try {
            try {
                BadInitNoStringInit.dummy = 1;
                throw new IllegalStateException("Should not reach here.");
            } catch (BadErrorNoStringInit e) {
                System.out.println(e);
            }

            // Check if it works a second time.

            try {
                BadInitNoStringInit.dummy = 1;
                throw new IllegalStateException("Should not reach here.");
            } catch (NoClassDefFoundError e) {
                System.out.println(e);
                System.out.println(e.getCause());
            }
        } catch (Exception error) {
            error.printStackTrace(System.out);
        }
    }
}
