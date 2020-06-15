package concurrent.phase2;

/**
 * 仆从类是真正干活的
 */
class Servant implements ActiveObj {

    @Override
    public Result makeString(int count, char fillChar) {
        char[] buffer = new char[count];
        for (int i = 0; i < count; i++) {
            buffer[i] = fillChar;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new RealResult(new String(buffer));
    }

    @Override
    public void displayString(String text) {
        try {
            System.out.println("Display: " + text);
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
