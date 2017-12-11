package application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class SampleController {
	@FXML
	private TextField txNumero;
	@FXML
	private RadioButton rbDecimal;
	@FXML
	private RadioButton rbRomano;
	@FXML
	private Button btnConverter;
	@FXML
	private Label lblOutput;
	private ToggleGroup group = new ToggleGroup();
	private static Map<Integer, Character> map = new LinkedHashMap<Integer, Character>();

	@FXML
	public void initialize() {
		loadMap();
		initRadioButton();
	}

	public void loadMap() {
		map.put(1, 'I');
		map.put(5, 'V');
		map.put(10, 'X');
		map.put(50, 'L');
		map.put(100, 'C');
		map.put(500, 'D');
		map.put(1000, 'M');
	}

	public void initRadioButton() {
		rbDecimal.setToggleGroup(group);
		rbDecimal.setSelected(true);
		rbRomano.setToggleGroup(group);
	}

	public void initButton() {
		btnConverter.setOnAction(e -> {
			if (rbDecimal.isSelected()) {
				toDecimal();
			} else
				toRoman();
		});
	}

	public boolean isDecimal(String s) {
		return Pattern.matches("\\d+", s);
	}

	public boolean isRoman(String s) {
		return Pattern.matches("^M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$", s);
	}

	public void toRoman() {
		if (isDecimal(txNumero.getText())) {
			lblOutput.setText(getRoman(Integer.parseInt(txNumero.getText())));
		} else
			showMessage();
	}

	public static String getRoman(int input) {
		String output = "";
		while (input > 0) {
			int bigger = 0;
			for (Integer entry : map.keySet())
				if (entry <= input)
					bigger = entry;
			
			output += map.get(bigger);
			input -= bigger;
		}
		
		output = checkConditions(output);
		return output;
	}
	
	public static String checkConditions(String output) {
		String aux = output;
		if(aux.contains("IIII"))
			aux = aux.replaceAll("IIII", "IV");
		if(aux.contains("VIV"))
			aux = aux.replaceAll("VIV", "IX");
		if(aux.contains("XXXX"))
			aux = aux.replaceAll("XXXX", "XL");
		if(aux.contains("LXL"))
			aux = aux.replaceAll("LXL", "XC");
		if(aux.contains("CCCC"))
			aux = aux.replaceAll("CCCC", "CD");
		if(aux.contains("DCD"))
			aux = aux.replaceAll("DCD", "CM");
		
		return aux;
	}

	public void toDecimal() {
		String convert = txNumero.getText().toUpperCase();
		List<Integer> list = new ArrayList<Integer>();
		if (isRoman(convert)) {
			for (int i = convert.length(); i != 0; i--)
				for (Entry<Integer, Character> entry : map.entrySet())
					if (entry.getValue().equals(convert.charAt(i - 1)))
						list.add((Integer) entry.getKey());
		} else
			showMessage();

		Integer result = getDecimal(list);
		if (result > 0)
			lblOutput.setText(result.toString());
	}

	public Integer getDecimal(List<Integer> list) {
		Integer total = 0;
		int i = 0;
		while (i < list.size()) {
			if ((i + 1) < list.size() && (list.get(i) > list.get(i + 1))) {
				total += list.get(i) - list.get(i + 1);
				i++;
			} else
				total += list.get(i);
			i++;
		}
		return total;
	}

	public void showMessage() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Conversão de Valores");
		alert.setHeaderText("Valor inválido!");
		alert.setContentText("Favor inserir um valor válido.");
		alert.showAndWait();
	}
}