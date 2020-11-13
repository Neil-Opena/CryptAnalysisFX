/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptanalysisfx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author neil1
 */
public class CryptAnalysisFX extends Application {

	private BorderPane root;
	private VBox left;
	private HBox right;
	private TextArea textArea;
	private Button trigraphBtn;
	private Button digraphBtn;
	private ArrayList<LinkedList> data;
	private int frequent;
	private TextField colSize;
	private Button arrayBtn;
	private HBox arrayContainer;
	private ArrayList<TextField> input;
	private Button computeSampleIC;
	private Button buildCount4;
	private Button buildCount5;
	private HBox keyword2Container;
	private TextField keyword2;
	private Button keyword2Translate;

	private String code;
	private Button translate;

	@Override
	public void start(Stage primaryStage) {

		root = new BorderPane();
		left = new VBox();
		left.setSpacing(20);
		right = new HBox();
		right.setSpacing(20);
		root.setLeft(left);
		root.setCenter(right);

		textArea = new TextArea();
		textArea.setPrefSize(300, 500);
		textArea.setWrapText(true);
		trigraphBtn = new Button("Trigraph");
		trigraphBtn.setOnAction(event -> {
			right.getChildren().clear();
			initData();
			for (int i = 0; i < 26; i++) {
				VBox tempBox = new VBox();

				Label size = new Label("" + (data.get(i).size() - 1));
				tempBox.getChildren().add(size);

				for (int j = 0; j < data.get(i).size(); j++) {
					String group = (String) data.get(i).get(j);
					Label toAdd = new Label(group);
					toAdd.autosize();
					if (j == 0) {
						toAdd.setStyle("-fx-font-weight: bold;");
					}

					for (int k = 0; k < data.get(i).size(); k++) {
						if (data.get(i).get(k).equals(group) && j != k) {
							toAdd.setUnderline(true);
							break;
						}
					}
					//underline if group is repeated in the list

					tempBox.getChildren().add(toAdd);
				}
				right.getChildren().add(tempBox);
			}
		});

		digraphBtn = new Button("Digraph");
		digraphBtn.setOnAction(event -> {
			right.getChildren().clear();
			initData();
			for (int i = 0; i < 26; i++) {
				ArrayList<String> before = new ArrayList<>();
				ArrayList<String> after = new ArrayList<>();

				VBox tempBox = new VBox();
				int size = data.get(i).size() - 1;
				if (size <= frequent) {
					continue;
				}

				for (int j = 1; j < data.get(i).size(); j++) {
					before.add("" + ((String) data.get(i).get(j)).charAt(0));
					after.add("" + ((String) data.get(i).get(j)).charAt(1));
				}

				tempBox.getChildren().add(new Label("" + size));
				String head = (String) data.get(i).get(0);
				Label headLabel = new Label(head);
				headLabel.setStyle("-fx-font-weight: bold;");
				tempBox.getChildren().add(headLabel);

				Label bef = new Label("Bef:");
				Label aft = new Label("Aft:");
				bef.setUnderline(true);
				aft.setUnderline(true);
				tempBox.getChildren().addAll(new Label("\n"), bef);

				HashSet set = new HashSet();
				for (int j = 0; j < before.size(); j++) {
					set.add(before.get(j));
				}

				List list = Arrays.asList(set.toArray());
				list.sort((Object a, Object b) -> {
					return ((String) a).compareTo((String) b);
				});
				list.forEach(letter -> {
					int count = 0;
					for (int k = 0; k < before.size(); k++) {
						if (((String) letter).equals(before.get(k))) {
							count++;
						}
					}
					if (count >= 2) {
						tempBox.getChildren().add(new Label("" + letter + count));
					}
				});

				set.clear();
				for (int j = 0; j < after.size(); j++) {
					set.add(after.get(j));
				}

				tempBox.getChildren().addAll(new Label("\n"), aft);

				list = Arrays.asList(set.toArray());
				list.sort((Object a, Object b) -> {
					return ((String) a).compareTo((String) b);
				});
				list.forEach(letter -> {
					int count = 0;
					for (int k = 0; k < after.size(); k++) {
						if (((String) letter).equals(after.get(k))) {
							count++;
						}
					}
					if (count >= 2) {
						tempBox.getChildren().add(new Label("" + count + letter));
					}
				});

				right.getChildren().add(tempBox);
			}
		});
		left.setSpacing(20);
		left.setPadding(new Insets(30));

		colSize = new TextField();
		colSize.setPrefWidth(50);
		arrayBtn = new Button("Generate Array");
		colSize.setText("6");

		arrayBtn.setOnAction(action -> {
			int col = Integer.parseInt(colSize.getText());
			generate(col);
		});

		arrayContainer = new HBox();
		arrayContainer.setSpacing(10);
		arrayContainer.getChildren().addAll(colSize, arrayBtn);

		computeSampleIC = new Button("Compute Sample IC");
		computeSampleIC.setOnAction(event -> {
			computeSampleIC();
		});

		buildCount4 = new Button("Build frequency - 4");
		buildCount4.setOnAction(event -> {
			buildFrequency(4);
		});

		buildCount5 = new Button("Build frequency - 5");
		buildCount5.setOnAction(event -> {
			buildFrequency(5);
		});

	
		keyword2 = new TextField();
		keyword2Translate = new Button("Translate (2nd keyword)");
		keyword2Translate.setOnAction(event -> {
			keyword2Translate(keyword2.getText());
		});

		keyword2Container = new HBox();
		keyword2Container.setSpacing(10);
		keyword2Container.getChildren().addAll(keyword2, keyword2Translate);

		left.getChildren().addAll(textArea, trigraphBtn, digraphBtn, arrayContainer, computeSampleIC, buildCount4, buildCount5, keyword2Container);

		Scene scene = new Scene(root, 300, 250);

		primaryStage.setTitle("CryptAnalysis");
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(1000);
		primaryStage.setMinWidth(1500);
		primaryStage.show();
	}

	private void keyword2Translate(String keyword){
		initData();
		String[] alphabets = new String[keyword.length()];
		for(int i = 0; i < keyword.length(); i++){
			StringBuilder builder = new StringBuilder();
			for(int j = 0; j < 26; j++){
				int temp = j;
				if(keyword.charAt(i) + j > 'Z'){
					temp = j - 26;
				}
				//alphabets[i][j] = (char) ((keyword.charAt(i) + temp));
				//91 --> 0
				builder.append((char) ((keyword.charAt(i) + temp)));
			}
			alphabets[i] = builder.toString();
		}

		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < code.length(); i++){
			int row = i % keyword.length();
			//find index of current letter
			char toTest = code.charAt(i);
			int index = alphabets[row].indexOf(toTest);
			//T --> B 
			builder.append((char)('A' + index));
		}
		textArea.setText(builder.toString());
	}

	private void buildFrequency(int n) {
		initData();
		/*
			Split into 4 or 5 alphabets and compute I.C. for each alphabet: I.C.'s should be ~0.065 + 
			(if 4 alphabets, first alphabet uses letters in positions 1, 5, 9, ... 4k + 1
			second alphabet uses 2, 6, 10, ... 4k + 2
			third alphabet uses 3, 7, 11, ... 4k + 3
			fourth alphabet uses 4, 8, 12, .... 4k)
		 */
		//this works now
		int[][] alphabets = new int[n][26];
		for (int i = 0; i < n; i++) {
			for (int j = 0 + i; j < code.length(); j += (n)) {
				int index = (int) (code.charAt(j) - 'A');
				alphabets[i][index]++;
			}
		}

		for (int i = 0; i < n; i++) {
			//find the IC for each alphabet
			//should also print letters 
			Stage tempStage = new Stage();
			HBox root = new HBox();
			tempStage.setScene(new Scene(root));

			Label alphabetIC = new Label(computeIC(alphabets[i]));
			root.getChildren().addAll(new Label(i + ".) "), alphabetIC);

			for (int j = 0; j < alphabets[i].length * 2; j++) {
				VBox temp = new VBox();
				temp.setStyle("-fx-border-color: black");
				temp.setPrefWidth(30);
				Label letter = new Label(("" + (char) ('A' + (j % 26))));
				letter.setPrefWidth(30);
				letter.setAlignment(Pos.CENTER);
				Label frequency = new Label(alphabets[i][j % 26] + "");
				if(alphabets[i][j % 26] == 0){
					frequency.setTextFill((Paint.valueOf("red")));
				}
				frequency.setPrefWidth(30);
				frequency.setAlignment(Pos.CENTER);
				temp.setAlignment(Pos.CENTER);
				temp.getChildren().addAll(letter, frequency);
				root.getChildren().addAll(temp);
			}
			root.setSpacing(0);
			tempStage.setAlwaysOnTop(true);
			tempStage.setResizable(false);
			tempStage.show();
		}
	}

	private String computeIC(int[] alphabet) {
		//each alphabet is essentially a frequency
		int n4 = 0;
		int sum4 = 0;
		for (int i = 0; i < alphabet.length; i++) {
			if (alphabet[i] == 0) {
				continue;
			}
			int f_i = alphabet[i];
			sum4 += (f_i) * (f_i - 1);
			n4 += f_i;
		}
		double IC = sum4 * (1.0 / (n4 * (n4 - 1)));
		return String.format("%.2f", IC);
	}

	private void computeSampleIC() {
		/*
			compute I.C. = 1/N(N - 1) * Summation (f_i (f_i - 1))
			about 0.065 + for 1 alphabet
			about 0.044 for 5 alphabets
		 */

 /*
			Tips by professor:
			1. The frequencies f (subscript i) used in computing the I.C. are the frequencies of each letter, 
			e.g., A, B, C, etc. in the SUBSET of letters from the cryptogram that you are counting. 
			For example, if in counting frequencies among the first letter in every group of 5, you find that 
			there are 3 E's, then f (subscript E) = 3. Also, N is the total number of letters in the SUBSET you 
			counting.

			2. DO NOT compute the I.C. of the complete message. Only compute 
			it for the first letter in every group of 4 letters, first letter in every group of 5 letters, and first 
			letter in every group of 6 letters.

			3. When one of the three groups tallied in 2. gives a high I.C., say the first in every 6 letters, 
			then build frequency counts for the 2nd letter in every group of 6 letter, for the 3rd letter 
			in every group of 6, then for the 4th, then for the 5th and then for the 6th. Compute the I.C. for 
			each of the other 5 frequency distributions and make sure that the average of the 6 IC's is over .06.
		 */
		int[] frequencies4 = new int[26]; //hold frequencies of letters
		int[] frequencies5 = new int[26]; //hold frequencies of letters
		initData();
		StringBuilder builder4 = new StringBuilder();
		StringBuilder builder5 = new StringBuilder();
		//test for first letter in every group of 4
		for (int i = 0; i < code.length(); i += 4) {
			builder4.append(code.charAt(i));
		}
		//test for first letter in every group of 5
		for (int i = 0; i < code.length(); i += 5) {
			builder5.append(code.charAt(i));
		}

		String toCount4 = builder4.toString();
		String toCount5 = builder5.toString();
		for (int i = 0; i < toCount4.length(); i++) {
			int index = (int) (toCount4.charAt(i) - 'A');
			frequencies4[index]++;
		}
		for (int i = 0; i < toCount5.length(); i++) {
			int index = (int) (toCount5.charAt(i) - 'A');
			frequencies5[index]++;
		}

		int n4 = 0;
		int sum4 = 0;
		for (int i = 0; i < frequencies4.length; i++) {
			if (frequencies4[i] == 0) {
				continue;
			}
			int f_i = frequencies4[i];
			sum4 += (f_i) * (f_i - 1);
			n4 += f_i;
		}
		double IC4 = sum4 * (1.0 / (n4 * (n4 - 1)));

		int n5 = 0;
		int sum5 = 0;
		for (int i = 0; i < frequencies5.length; i++) {
			if (frequencies5[i] == 0) {
				continue;
			}
			int f_i = frequencies5[i];
			sum5 += (f_i) * (f_i - 1);
			n5 += f_i;
		}
		double IC5 = sum5 * (1.0 / (n5 * (n5 - 1)));

		Stage newStage = new Stage();
		VBox root = new VBox();

		Label sample4 = new Label("2nd keywork length 4 I.C. = " + IC4);
		Label sample5 = new Label("2nd keywork length 5 I.C. = " + IC5);

		root.getChildren().addAll(sample4, sample5);
		newStage.setScene(new Scene(root));
		newStage.setMinHeight(300);
		newStage.setMinWidth(300);
		newStage.show();
	}

	private void generate(int col) {
		Stage newStage = new Stage();
		GridPane root = new GridPane();
		root.setVgap(10);
		root.setHgap(10);
		int row = (int) Math.ceil(26.0 / col);
		int rem = 26 % col;
		input = new ArrayList<>();
		int c = 0;
		for (int i = 0; i < col; i++) {
			if (i < rem) {
				for (int j = 0; j < row; j++, c++) {
					VBox temp = new VBox();
					temp.setAlignment(Pos.CENTER);
					temp.setPrefSize(70, 70);
					VBox.setMargin(temp, new Insets(10));

					TextField charInput = new TextField();
					charInput.setMaxWidth(40);
					input.add(charInput);

					Label charLetter = new Label("" + (char) ('A' + c));
					charLetter.setStyle("-fx-font-weight: bold");
					temp.getChildren().addAll(charLetter, charInput);
					root.add(temp, i, j);
				}
			} else {
				for (int j = 0; j < row - 1; j++, c++) {
					VBox temp = new VBox();
					temp.setAlignment(Pos.CENTER);
					temp.setPrefSize(70, 70);
					VBox.setMargin(temp, new Insets(10));

					TextField charInput = new TextField();
					charInput.setMaxWidth(40);
					input.add(charInput);

					Label charLetter = new Label("" + (char) ('A' + c));
					charLetter.setStyle("-fx-font-weight: bold");
					temp.getChildren().addAll(charLetter, charInput);
					root.add(temp, i, j);
				}
			}
		}

		translate = new Button("Translate");
		translate.setOnAction(event -> translate());
		root.add(translate, col, row);

		newStage.setScene(new Scene(root));
		newStage.setMinHeight(450);
		newStage.setMinWidth(450);
		newStage.show();
	}

	private void translate() {
		initData();
		Stage stage = new Stage();

		//textfield "blanks" are the english text
		//column text or labels are the code text

		//so when translated --> taecode text (code) and parse it
		//for each letter in the code text, go to specific label and retrieve the label for that

		StringBuilder decoderBuilder = new StringBuilder();

		input.forEach(textField -> {
			String character = textField.getText().trim();
			if (textField.getText().isEmpty()) {
				character = "_";
			}
			decoderBuilder.append(character);
		});
		String decoder = decoderBuilder.toString();

		StringBuilder messageBuilder = new StringBuilder();
		for(int i = 0; i < code.length(); i++){
			char toTranslate = code.charAt(i);
			int index = (int) (toTranslate - 'A');
			//go to decoder and get that letter on that index
			messageBuilder.append(decoder.charAt(index));
		}

		Label text = new Label(messageBuilder.toString());
		text.setWrapText(true);
		text.setTextAlignment(TextAlignment.JUSTIFY);

		StackPane displ = new StackPane(text);
		displ.setPadding(new Insets(10));

		Scene scene = new Scene(displ, 400, 400);
		stage.setScene(scene);
		stage.show();
	}

	private void initData() {
		code = textArea.getText().trim();
		code = String.join("", code.split(" "));
		code = String.join("", code.split("\n"));
		frequent = (code.length() / 26) / 2;
		data = new ArrayList<LinkedList>();

		for (int i = 0; i < 26; i++) {
			LinkedList<String> letter = new LinkedList<String>();
			letter.add("" + ((char) ('A' + i)));
			data.add(letter);
		}

		for (int i = 0; i < code.length(); i++) {
			char before;
			if ((i - 1) < 0) {
				before = '.';
			} else {
				before = (char) code.charAt(i - 1);
			}
			char after;
			if ((i + 1) >= code.length()) {
				after = '.';
			} else {
				after = (char) code.charAt(i + 1);
			}
			char head = code.charAt(i);
			data.get(head - 'A').add("" + before + after);
		}

	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
