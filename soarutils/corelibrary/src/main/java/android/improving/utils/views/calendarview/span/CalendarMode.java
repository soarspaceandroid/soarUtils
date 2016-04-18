package android.improving.utils.views.calendarview.span;

public enum CalendarMode {

    MONTHS(6),
    WEEKS(1);

    public final int visibleWeeksCount;

    CalendarMode(int visibleWeeksCount) {
        this.visibleWeeksCount = visibleWeeksCount;
    }
}
