package com.meneguello.jira;

import java.util.StringTokenizer;

import javafx.util.StringConverter;

import org.apache.commons.lang.StringUtils;

public class IntervalStringConverter extends StringConverter<Interval> {
	
	protected class IntervalState {
		public int weeks, days, hours, minutes;
	}

	@Override
	public String toString(Interval interval) {
		if (interval == null) {
			return null;
		}
		
		final StringBuilder sb = new StringBuilder();
		append(sb, interval.getWeeks(), "w");
		append(sb, interval.getDays(), "d");
		append(sb, interval.getHours(), "h");
		append(sb, interval.getMinutes(), "m");
		return sb.toString();
	}

	private void append(final StringBuilder sb, final int amount, final String unity) {
		if (amount > 0) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(amount + unity);
		}
	}

	@Override
	public Interval fromString(String string) {
		final IntervalState state = new IntervalState();
		final StringTokenizer tokenizer = new StringTokenizer(string);
		while (tokenizer.hasMoreTokens()) {
			final String token = StringUtils.trimToEmpty(tokenizer.nextToken());
			if (StringUtils.isNumeric(token)) {
				parseHuman(state, Integer.parseInt(token), tokenizer.nextToken());
			} else {
				if (token.endsWith("w")) {
					state.weeks = Integer.parseInt(token.substring(0, token.length() - 1));
				} else if (token.endsWith("d")) {
					state.days = Integer.parseInt(token.substring(0, token.length() - 1));
				} else if (token.endsWith("h")) {
					state.hours = Integer.parseInt(token.substring(0, token.length() - 1));
				} else if (token.endsWith("m")) {
					state.minutes = Integer.parseInt(token.substring(0, token.length() - 1));
				} else {
					throw new IllegalArgumentException("Invalid time unity \"" + token + "\"");//TODO: i18n
				}
			}
		}
		return new Interval(state.weeks, state.days, state.hours, state.minutes);
	}

	private int parseHuman(IntervalState state, int amount, String unity) {
		if (unity.startsWith("weeks") || unity.startsWith("week") || unity.startsWith("semanas") || unity.startsWith("semana")) {
			return state.weeks = amount;
		} else if (unity.startsWith("days") || unity.startsWith("day") || unity.startsWith("dias") || unity.startsWith("dia")) {
			return state.days = amount;
		} else if (unity.startsWith("hours") || unity.startsWith("hour") || unity.startsWith("horas") || unity.startsWith("hora")) {
			return state.hours = amount;
		} else if (unity.startsWith("minutes") || unity.startsWith("minute") || unity.startsWith("minutos") || unity.startsWith("minuto")) {
			return state.minutes = amount;
		}
		throw new IllegalArgumentException("Invalid time unity \"" + amount + " " + unity.replaceAll(",", "") + "\"");//TODO: i18n
	}

}
