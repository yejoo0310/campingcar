package view;

import java.time.LocalDate;

/** CalendarView가 날짜를 클릭했을 때 호출되는 콜백 */
@FunctionalInterface
public interface DateSelectionListener {
	void onDateSelected(LocalDate date);
}
