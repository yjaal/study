package concurrent.phase1;

public class Thread9 {

    public static void main(String[] args) {
        final TicketWindowRunnable ticketWindow = new TicketWindowRunnable();
        Thread win1 = new Thread(ticketWindow, "一号窗口");
        Thread win2 = new Thread(ticketWindow, "二号窗口");
        Thread win3 = new Thread(ticketWindow, "三号窗口");
        win1.start();
        win2.start();
        win3.start();
    }
}
