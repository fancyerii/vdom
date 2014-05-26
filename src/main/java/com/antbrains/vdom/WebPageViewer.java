package com.antbrains.vdom;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

class DrawPanel extends JPanel {
	private static final long serialVersionUID = -9127980986662779748L;

	public DrawPanel(VDomNode root) {
		this.root = root;
	}

	private Font defaultFont;

	private void drawNodeRect(Graphics2D g2, VDomNode curNode) {
		int x1 = (int) curNode.getLeft();
		int y1 = (int) curNode.getTop();
		int x2 = (int) (x1 + curNode.getWidth());
		int y2 = y1;
		g2.drawLine(x1, y1, x2, y2);
		int x3 = x2;
		int y3 = (int) (y1 + curNode.getHeight());
		g2.drawLine(x2, y2, x3, y3);
		int x4 = x1;
		int y4 = y3;
		g2.drawLine(x3, y3, x4, y4);
		g2.drawLine(x4, y4, x1, y1);
	}

	private String[] wrapLines(String text, int lineNumber) {
		if (text == null || text.length() < 1) {
			return new String[] { "" };
		}
		int total = text.length();
		int avg = total / lineNumber;
		String[] result = new String[lineNumber];
		int firstLineChar = total - avg * (lineNumber - 1);
		result[0] = text.substring(0, firstLineChar);
		int start = firstLineChar;
		for (int i = 1; i < lineNumber; i++) {
			result[i] = text.substring(start, start + avg);
			start += avg;
		}
		return result;
	}

	private void recurDraw(Graphics2D g2, VDomNode curNode) {
		if (curNode == null)
			return;
		if (curNode.getWidth() > 0 && curNode.getHeight() > 0) {
			if (curNode.getTag() == null) {// text node
				int fontSize = curNode.getFontSize();
				String text = curNode.getText();
				if (fontSize > 0) {
					g2.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
				} else {
					g2.setFont(defaultFont);
				}

				// this.drawNodeRect(g2, parent);
				this.drawNodeRect(g2, curNode);
				int lineNumber = (int) (curNode.getHeight() / curNode
						.getFontSize());
				if (lineNumber < 1) {
					lineNumber = 1;
				}
				String[] lines = wrapLines(text, lineNumber);
				int left = (int) curNode.getLeft();
				int top = (int) curNode.getTop();
				for (int i = 0; i < lines.length; i++) {
					top += curNode.getFontSize();
					g2.drawString(lines[i], left, top);
				}
				// System.out.println(text+" x1: "+x1+", y1: "+y1+" p.x1: "+parent.getLeft()+", p.y1: "+parent.getTop()+", p.tag: "+parent.getTag());

			} else {
				this.drawNodeRect(g2, curNode);
			}
		}
		for (VDomNode child : curNode.getChildren()) {
			recurDraw(g2, child);
		}

	}

	private VDomNode root;

	public void setRoot(VDomNode root) {
		this.root = root;
	}

	private void doDrawing(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (defaultFont == null) {
			defaultFont = g2.getFont();
		}
		g2.setStroke(new BasicStroke(2));
		recurDraw(g2, root);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
}

public class WebPageViewer extends JFrame {

	private static final long serialVersionUID = -2819583443665614153L;
	private VDomDumper vdd;
	private int width = 800;
	private int height = 600;
	private VDomNode root;
	private DrawPanel dp;

	public void close() {
		vdd.close();
	}

	public WebPageViewer() throws IOException {
		// double[] rect=VDomNode.getContainingRect(root);
		// System.out.println("left: "+rect[0]+", top: "+rect[1]+", right: "+rect[2]+", bottom: "+rect[3]);
		vdd = new VDomDumper(true, false);
		initUI();
	}

	public final void initUI() {
		setLayout(new BorderLayout());
		dp = new DrawPanel(root);
		dp.setPreferredSize(new Dimension(width, height));

		setTitle("WebPageViewer");
		// setLocationRelativeTo(null);
		final JScrollPane scrollPane = new JScrollPane(dp);
		final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		// int w = Math.min(d.width, this.width + 20);
		// int h = Math.min(d.height, this.height + 20);

		scrollPane.setPreferredSize(d);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		JPanel urlPanel = new JPanel();
		final JTextField txtUrl = new JTextField(100);
		txtUrl.setPreferredSize(new Dimension(200, 25));
		urlPanel.setLayout(new FlowLayout());
		urlPanel.add(txtUrl);
		final JButton btn = new JButton("浏览");
//		btn.setPreferredSize(new Dimension(50, 25));
		urlPanel.add(btn);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					String url = txtUrl.getText();
					if (!url.startsWith("http")) {
						url = "http://" + url;
					}
					final String u=url;
					txtUrl.setEditable(false);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							root = vdd.dump(u);
							if(root!=null){
								dp.setRoot(root);
								double[] rect=VDomNode.getContainingRect(root);
								width=(int)rect[2];
								height=(int)rect[3];
								dp.setPreferredSize(new Dimension(width, height));
								dp.setSize(width, height);
								int w = Math.min(d.width, width + 20);
								int h = Math.min(d.height, height + 20);
								scrollPane.setPreferredSize(new Dimension(w, h));
								repaint();
							}
							txtUrl.setEditable(true);
						}
					});

				} catch (Exception ex) {
				}

			}
		});
		add(urlPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws Exception {
		final WebPageViewer wpv = new WebPageViewer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					wpv.pack();
					wpv.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		wpv.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				wpv.close();
			}
		});
	}
}