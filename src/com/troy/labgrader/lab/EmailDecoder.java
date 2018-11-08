package com.troy.labgrader.lab;

import java.io.*;
import java.util.*;

import com.troy.labgrader.StudentList;
import com.troy.labgrader.email.*;

public class EmailDecoder {
	private static final String PERIOD = "period", FIRST_NAME = "first_name", LAST_NAME = "last_name", LAB = "lab";

	public static Lab narrowDownLab(Year year, List<Lab> avilableLabs, StudentList allStudents, Email email) {
		try {
			String body = email.getBody();
			Map<String, String> commands = getKeysAndValues(body, '#', '=');
			List<Student> possibleStudents = new ArrayList<Student>();
			for (Student student : allStudents.getStudents()) {// There are a couple things we have to do here get a list of all the students who match the same email
				if (student.hasEmail(email.getFrom())) {
					possibleStudents.add(student);
				}
			}
			if (commands.containsKey(PERIOD)) {
				try {
					int period = Integer.parseInt(commands.get(PERIOD));
					boolean found = false;
					for (Course course : year.getCourses()) {
						if (course.getPeroids().contains(period))
							found = true;
					}
					if (!found)
						throw new EmailRejectedException("No course exists for period \"" + period + "\"");
					Iterator<Student> it = possibleStudents.iterator();
					while (it.hasNext()) {
						Student student = it.next();
						if (student.getPeriod() != period) {
							it.remove();// Remove any student whose period doesn't match match the one provided
							System.out.println("removing peroid doesnt work " + student);
						}
					}
				} catch (Exception e) {
					throw new EmailRejectedException("Non numerical period \"" + commands.get(PERIOD) + "\"");
				}
			}
			if (commands.containsKey(FIRST_NAME) && commands.containsKey(LAST_NAME)) {// first and last
				String fullName = commands.get(FIRST_NAME) + ' ' + commands.get(LAST_NAME);
				Iterator<Student> it = possibleStudents.iterator();
				while (it.hasNext()) {
					Student student = it.next();
					if (!student.getName().equalsIgnoreCase(fullName)) {
						it.remove();
						System.out.println("removing because name doesnt match " + student);
					}
				}
			} else if (commands.containsKey(FIRST_NAME)) {// just first
				Iterator<Student> it = possibleStudents.iterator();
				while (it.hasNext()) {
					Student student = it.next();
					if (!student.getName().toLowerCase().contains(commands.get(FIRST_NAME).toLowerCase())) {
						it.remove();
						System.out.println("removing because first name doesnt match " + student);
					}
				}
			} else if (commands.containsKey(LAST_NAME)) {// just last
				Iterator<Student> it = possibleStudents.iterator();
				while (it.hasNext()) {
					Student student = it.next();
					if (!student.getName().toLowerCase().contains(commands.get(LAST_NAME).toLowerCase())) {
						it.remove();
						System.out.println("removing because last name doesnt match " + student);
					}
				}
			}
			if (possibleStudents.size() != 1) {
				throw new EmailRejectedException("Cannot determine who sent this lab!\nCheck with Mr. Lau to make sure that the email you sent with this is in his database");
			}
			Student student = possibleStudents.get(0);// We know that we have narrowed it down to one student
			Course course = null;
			for (Course c : year.getCourses()) {
				if (c.getPeroids().contains(student.getPeriod())) {
					course = c;
				}
			}
			if (course == null)
				throw new EmailRejectedException("Student " + student.getName() + " id: " + student.getId() + " is not in any courses");

			List<Lab> possibleLabs = new ArrayList<Lab>();
			possibleLabs.addAll(course.getLabs());

			Iterator<Lab> it = possibleLabs.iterator();
			while (it.hasNext()) {
				Lab lab = it.next();
				if (!lab.getData().isOpen(email.getTimeRecieved())) {
					it.remove();
					System.out.println("cand be lab " + lab + " because submissions arent open");
				}
			}
			if (possibleLabs.size() > 1) {// We need to narrow down which lab to use based on the name
				if(commands.containsKey(LAB)) {
					String emailedName = commands.get(LAB);
					Iterator<Lab> it 
				}
			}
			if(possibleLabs.size() > 1) {
				throw new EmailRejectedException("Cannot determine whitch you are tying to submit for, current labs include: " + possibleLabs);
			}
			if (possibleLabs.isEmpty()) {
				throw new EmailRejectedException("There are no labs that are open for submissions right now.");
			}

		} catch (EmailRejectedException e) {
			email.reply("Lab rejected!\n" + e.getReason(), true);
		}
		return null;
	}

	public static Map<String, String> getKeysAndValues(String text, char lineStart, char split) {

		List<String> lines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new StringReader(text));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (Exception e) {
		}
		Iterator<String> it = lines.iterator();
		while (it.hasNext()) {// Remove all lines that dont have '='
			line = it.next();
			if (!line.startsWith("" + lineStart) || !line.contains("" + split))
				it.remove();
		}
		final HashMap<String, String> commands = new HashMap<String, String>();
		lines.forEach((l) -> {
			int index = l.indexOf('=');
			if (index == -1)
				return;
			String key = l.substring(1, index);
			String value = l.substring(index + 1);
			key = key.toLowerCase();
			commands.put(key, value);
		});
		return commands;
	}
}
