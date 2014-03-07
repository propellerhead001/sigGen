package sig_gen;
import gnu.io.NRSerialPort;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.*;
/**
 * @author Robert Mills
 *
 */
public class Core {
	enum SettingState {
		NONE, SWEEP, FREQMOD, AMPMOD, NORMAL}
	enum FunctionState{
		NONE, SINEWAVE, SQUAREWAVE, TRIWAVE, ARBFUNC, FREQCOUNT}

	private SettingState currentSetting = SettingState.NONE;
	private FunctionState currentFunction = FunctionState.NONE;
	//Serial Communication settings
	static String comPort;
	static int baud = 921600;
	static NRSerialPort serial;
	static DataOutputStream out;
	//Flag to show when a COM port has been selected
	static boolean selectionMade = false;
	public Core(){
		getComPort();
		while(!selectionMade);
		boolean isConnected = setupUSB(comPort, baud);
		if(isConnected){
			openWindow();
		}
		else{
			showFailiure();
		}
	}
	public void main(String[] args){
		new Core();
	}

	/**
	 * 
	 */
	private void openWindow() {
		JFrame mainWindow = new JFrame("Signal Generator, Frequency Counter");
		JPanel function = new JPanel(new GridLayout(0,1));
		JPanel sSettings = new JPanel(new GridLayout(0,1));
		JPanel options = new JPanel(new GridLayout(0,1));
		createFunctionPanel(function, currentFunction);
		generatesSettingsPanel(sSettings, currentSetting);

		//Add panel borders
		function.setBorder(BorderFactory.createLineBorder(Color.black));
		sSettings.setBorder(BorderFactory.createLineBorder(Color.black));
		options.setBorder(BorderFactory.createLineBorder(Color.black));
		//Add panels
		mainWindow.getContentPane().add(function, BorderLayout.WEST);
		mainWindow.getContentPane().add(sSettings, BorderLayout.CENTER);
		mainWindow.getContentPane().add(options, BorderLayout.EAST);

		mainWindow.pack();
		mainWindow.setVisible(true);
	}

	/**
	 * @param sSettings
	 */
	private void generatesSettingsPanel(JPanel sSettings, SettingState currentState) {
		//Set Settings pane for Sine and Square Waves
		JRadioButton sSweep = new JRadioButton("Sweep");
		JRadioButton sAM = new JRadioButton("Amplitude Modulation");
		JRadioButton sFM = new JRadioButton("Frequency Modulation");
		JRadioButton sPlain = new JRadioButton("Normal");
		//Add buttons to group
		ButtonGroup sSettGroup = new ButtonGroup();
		sSettGroup.add(sPlain);
		sSettGroup.add(sSweep);
		sSettGroup.add(sAM);
		sSettGroup.add(sFM);
		//Add buttons to panel
		sSettings.add(new JLabel("Settings"));
		sSettings.add(sPlain);
		sSettings.add(sSweep);
		sSettings.add(sAM);
		sSettings.add(sFM);
	}

	/**
	 * Generates the panel for the system functions
	 * @param function The JPanel containing the function options for the system 
	 */
	private void createFunctionPanel(JPanel function, FunctionState functionState) {
		//set Function choices
		JRadioButton squareWave = new JRadioButton("Square Wave");
		JRadioButton sineWave = new JRadioButton("Sine Wave");
		JRadioButton triWave = new JRadioButton("Triangle Wave");
		JRadioButton arbFunct = new JRadioButton("Arbitrary Function");
		JRadioButton freqCounter = new JRadioButton("Frequency Counter");
		//add options to radio group
		ButtonGroup funcGroup = new ButtonGroup();
		funcGroup.add(squareWave);
		funcGroup.add(sineWave);
		funcGroup.add(triWave);
		funcGroup.add(arbFunct);
		funcGroup.add(freqCounter);
		//add to function pane
		function.add(new JLabel("Function"));
		function.add(squareWave);
		function.add(sineWave);
		function.add(triWave);
		function.add(arbFunct);
		function.add(freqCounter);
	}

	/**
	 * If the setupUSB has failed tells the user so
	 */
	private void showFailiure() {
		JFrame failiure = new JFrame("Connection Failiure");
		JTextField note = new JTextField("Connection Failiure, check connections and restart");
		failiure.getContentPane().add(note, BorderLayout.CENTER);
		failiure.pack();
		failiure.setVisible(true);
	}

	/**
	 * Sets up the COM or TTY port for use
	 * @param comPort2 The COM / TTY port the system is connected to
	 * @param baud2 The baud rate
	 * @return returns true if successful
	 */
	private boolean setupUSB(String comPort2, int baud2) {
		serial = new NRSerialPort(comPort2,baud);
		boolean success = serial.connect();
		return success;
	}

	/**
	 * Gets the COM / tty port the system is connected to
	 */
	private void getComPort() {
		final String[] availablePorts = {"COM1","COM2", "COM3", "COM4", "COM5", "COM6", "COM7"};
		final JFrame commWindow = new JFrame("Choose COM port");
		final JComboBox<String> selectCom = new JComboBox<String>(availablePorts);
		commWindow.getContentPane().add(selectCom, BorderLayout.NORTH);
		selectCom.setSelectedIndex(0);
		commWindow.pack();
		commWindow.setVisible(true);
		selectCom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comPort = availablePorts[selectCom.getSelectedIndex()];
				WindowEvent wev = new WindowEvent(commWindow, WindowEvent.WINDOW_CLOSING);
				Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
				selectionMade = true;
			}
		});
	}
}

