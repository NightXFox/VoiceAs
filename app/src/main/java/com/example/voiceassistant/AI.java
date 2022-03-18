package com.example.voiceassistant;

import android.os.Build;
import android.service.autofill.RegexValidator;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AI {
    protected static Map<Pattern, String> interactiveQuestions;
    protected static Map<String, String> answers;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getAnswer(String question) {
        String questionLower = question.toLowerCase();
        if (answers == null) {
            answers = new HashMap<>();
            answers.put("приветик", "Привет");
            answers.put("привет", "Привет");
            answers.put("как дела?", "Неплохо");
            answers.put("чем занимаешься?", "Отвечаю на вопросы глупых людишек)))");
            answers.put("а чем занимаешься?", "Отвечаю на вопросы глупых людишек)))");
        }
        String interactiveAnswer = getInteractiveAnswer(questionLower);
        if (interactiveAnswer == null)
            return answers.getOrDefault(questionLower, "Вопрос понял. Думаю...");
        else
            return interactiveAnswer;
    }
    private static Pattern getInteractiveQuestionPattern(String question) {
        if (interactiveQuestions == null) {
            interactiveQuestions = new HashMap<>();
            interactiveQuestions.put(Pattern.compile("^какой сегодня день\\??$"), "date");
            interactiveQuestions.put(Pattern.compile("^который час\\??$"), "hour");
            interactiveQuestions.put(Pattern.compile("^какой (сегодня )?день недели\\??$"), "weekday");
            interactiveQuestions.put(Pattern.compile("^сколько дней до (3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.(\\d{4})\\??$"), "days_before");
        }

        for (Map.Entry<Pattern, String> el : interactiveQuestions.entrySet()) {
            Matcher matcher = el.getKey().matcher(question);
            if (matcher.matches())
                return el.getKey();
        }
        return null;
    }
    private static String getInteractiveAnswer(String question) {
        Pattern pattern = getInteractiveQuestionPattern(question);
        if (pattern == null)
            return null;
        Calendar now = new GregorianCalendar();
        switch (interactiveQuestions.get(pattern)) {
            case "date":
                SimpleDateFormat formatterDate = new SimpleDateFormat("EEEE, d MMMM y года");
                return "Сегодня " + formatterDate.format(now.getTime());
            case "hour":
                SimpleDateFormat formatterHour = new SimpleDateFormat("H:mm");
                return "Сейчас " + formatterHour.format(now.getTime());
            case "weekday":
                SimpleDateFormat formatterWeekday = new SimpleDateFormat("EEEE");
                return "Сегодня " + formatterWeekday.format(now.getTime());
            case "days_before":
                Pattern datePattern = Pattern.compile("(3[01]|[12][0-9]|0[1-9])\\.(1[012]|0[1-9])\\.(\\d{4})");
                Matcher matcher = datePattern.matcher(question);
                String dateStr;
                if (matcher.find())
                    dateStr = question.substring(matcher.start(), matcher.end());
                else
                    return "Введённая дата имела неверный формат.";
                SimpleDateFormat formatterDateB = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    Date dateB = formatterDateB.parse(dateStr);
                    Calendar questionDate = Calendar.getInstance();
                    questionDate.setTime(dateB);
                    if (questionDate.before(now)) {
                        return "Эта дата уже прошла.";
                    }
                    else {
                        long left = (questionDate.getTime().getTime() - now.getTime().getTime()) / 1000 / 60 / 60 / 24 + 1;
                        String end;
                        if (left % 100 / 10 == 1) {
                            end = "дней";
                        }
                        else {
                            switch ((int) (left % 10)) {
                                case 1:
                                    end = "день";
                                    break;
                                case 2:
                                case 3:
                                case 4:
                                    end = "дня";
                                    break;
                                default:
                                    end = "дней";
                            }
                        }
                        return "До " + dateStr + " " + (end.equals("день") ? "остался" : "осталось") + " " + left + " " + end + ".";
                    }
                } catch (ParseException e) {
                    return "Произошла ошибка. Возможно, введённая дата имела неверный формат.";
                }
            default:
                return "Что-то пошло не так...";
        }
    }
}
