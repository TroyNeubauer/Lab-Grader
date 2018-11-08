package com.troy.labgrader.ui;

import java.awt.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import org.joda.time.LocalDateTime;

import com.troy.labgrader.*;
import com.troy.labgrader.email.Student;
import com.troy.labgrader.lab.*;

public class LabResultsViewer extends JPanel {

	private JTextArea labInfo = new JTextArea(15, 25);
	private DefaultListModel<LabResult> listModel = new DefaultListModel<LabResult>();
	private JList<LabResult> list = new JList<LabResult>(listModel);
	private Lab lab;
	private CourseViewer parent;
	private JScrollPane studentInfoScroll;

	public LabResultsViewer(Lab lab, CourseViewer parent) {
		super(new GridBagLayout());
		this.parent = parent;
		this.lab = lab;
		labInfo.setBorder(BorderFactory.createTitledBorder("Lab Information"));

		labInfo.setEditable(false);
		labInfo.setBackground(this.getBackground());
		labInfo.setFont(new Font("", Font.PLAIN, 13));
		StudentList students = parent.getStudentsInCourse();

		list.setCellRenderer(new ListCellRenderer<LabResult>() {

			@Override
			public Component getListCellRendererComponent(JList<? extends LabResult> list, LabResult value, int index, boolean isSelected, boolean cellHasFocus) {
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				Student student = students.getStudentWithID(value.id);
				if (student == null)
					panel.add(new JLabel("Invalid Student ID " + value.id));
				else {
					panel.add(new JLabel(student.getName() + " via " + student.getEmail1()));
					panel.add(new JLabel(MiscUtil.toProperEnglishName(value.runStatus.toString())));
				}
				if (isSelected)
					panel.setBackground(Color.LIGHT_GRAY);
				else
					panel.setBackground(Color.WHITE);
				return panel;
			}
		});
		list.addListSelectionListener((e) -> updateInfoPanel());

		GridBagConstraints c = new GridBagConstraints();
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridy = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.1;
		c.weighty = 3;
		JScrollPane scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(400, 1000));
		scroll.setMaximumSize(new Dimension(500, 10000));
		scroll.setMinimumSize(new Dimension(375, 500));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroll, c);

		c.gridwidth = 3;
		c.gridheight = 1;

		c.weightx = 1;
		c.weighty = 1;
		// c.anchor = GridBagConstraints.BASELINE_LEADING;
		// c.fill = GridBagConstraints.BOTH;

		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		studentInfoScroll = new JScrollPane();
		add(studentInfoScroll, c);

		c.gridx = 1;
		c.gridy = 1;
		add(labInfo, c);

		updateLabData();
		updateStudents();
		updateInfoPanel();
		Timer timer = new Timer((int) TimeUnit.SECONDS.toMillis(15), (e) -> {
			updateInfoPanel();
		});
		timer.setRepeats(true);
		timer.start();
	}

	public void updateStudents() {
		listModel.clear();
		for (Entry<Integer, LabResult> entry : lab.getResults().getMap().entrySet()) {
			listModel.addElement(entry.getValue());
		}
	}

	public void updateInfoPanel() {
		JPanel studentInfo = new JPanel();
		studentInfo.setBorder(BorderFactory.createTitledBorder("Submission Information"));
		studentInfo.setLayout(new BoxLayout(studentInfo, BoxLayout.Y_AXIS));
		if (list.getSelectedIndex() == -1) {
			studentInfo.add(new JLabel("Nothing Selected"));
		} else {
			LabResult result = listModel.get(list.getSelectedIndex());
			Student student = parent.getYearViewer().getYear().getStudents().getStudentWithID(result.id);
			studentInfo.add(new JLabel("Name: " + (student == null ? "Unknown" : student.getName())));
			studentInfo.add(new JLabel("Student ID: " + (student == null ? "Unknown" : student.getId())));
			studentInfo.add(new JLabel("Email: " + result.fromEmail));
			studentInfo.add(new JLabel("Plagiarism: " + result.plagiarism));
			studentInfo.add(new JLabel("Out Stream: " + result.stdOut));
			studentInfo.add(new JLabel("Error Stream: " + result.stdErr));
			studentInfo.add(new JLabel("Run Status: " + MiscUtil.toProperEnglishName(result.runStatus.toString())));
			studentInfo.add(new JLabel("Reset Submission"));
			JButton reset = new JButton("Reset");
			reset.addActionListener((e) -> {
				lab.getResults().getMap().put(student.getId(), new LabResult(student.getId()));
				updateStudents();
			});
			studentInfo.add(reset);
		}
		studentInfoScroll.setViewportView(studentInfo);
	}

	public void updateLabData() {
		StringBuilder sb = new StringBuilder();
		int graded = 0, compiled = 0, perfect = 0;
		for (LabResult result : lab.getResults().getMap().values()) {
			if (result.runStatus.compiled || result.runStatus == LabRunStatus.COMPILE_TIME_ERROR)
				graded++;
			if (result.runStatus.compiled)
				compiled++;
			if (result.runStatus == LabRunStatus.NOMINAL_EXECUTION)
				perfect++;
		}

		LocalDateTime open = lab.getData().getOpen(), close = lab.getData().getClose(), now = LocalDateTime.now();
		if (now.isBefore(open)) {
			sb.append("Submissions open in" + MiscUtil.getTimeDifference(now, open));
		} else if (now.isAfter(open) && now.isBefore(close)) {
			sb.append("Submissions close in " + MiscUtil.getTimeDifference(now, close));
		} else {
			sb.append("Submissions closed " + MiscUtil.getTimeDifference(now, close) + " ago");
		}
		sb.append('\n');

		sb.append("Out of ");
		sb.append(lab.getResults().getMap().size());
		sb.append(" labs...");
		sb.append('\n');

		sb.append(graded);
		sb.append(" labs have been graded");
		sb.append('\n');

		sb.append(compiled);
		sb.append(" labs have compiled");
		sb.append('\n');

		sb.append(perfect);
		sb.append(" labs ran perfectly");
		sb.append('\n');
		labInfo.setText(sb.toString());
	}

	public void periodsUpdated(Year year, Course course) {
		updateStudents();
		updateLabData();
	}
}
