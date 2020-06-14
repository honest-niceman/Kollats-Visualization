import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import java.util.LinkedList;

public class Graphs extends Application {
    private Stage primaryStage;
    private Scene scene;
    private Button button;
    private TextField Kollat;
    private LinkedList<Long> numbers;
    private ListView<String> langsListView;
    private XYChart.Series<String, Number> series;
    private int i = 0;
    public XYChart.Series<String, Number> getSeries() {
        return series;
    }
    public LinkedList<Long> getNumbers() {
        return numbers;
    }
    public long check() {
        try {
            return Long.parseLong(Kollat.getText());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public void calculate(long num) {
        numbers = new LinkedList<>();
        while (num != 1) {
            if (num % 2 == 0) {
                num = num / 2;
                numbers.add(num);
            } else {
                num = (3 * num) + 1;
                numbers.add(num);
            }
        }
    }

    public void print() {
        series.getData().add(new XYChart.Data<>("" + i, getNumbers().getFirst()));
        langsListView.getItems().add(""+(i+1)+") "+getNumbers().getFirst());
        getNumbers().removeFirst();
        i++;
    }

    private Parent graph(long n) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Кол-во операций");
        xAxis.setAnimated(false);
        yAxis.setLabel("Значение");
        yAxis.setAnimated(false);

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Гипотеза Коллатца in RealTime");
        lineChart.setAnimated(false);

        series = new XYChart.Series<>();
        getSeries().setName("Гипотеза Коллатца");

        lineChart.getData().add(getSeries());

        calculate(n);
        langsListView = new ListView<>();
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(100), (ActionEvent actionEvent) -> print())
        );
        timeline.setCycleCount(100);
        timeline.play();
        Button btn = new Button("Попробовать снова!");
        langsListView.getItems().add("Первое число: "+n);
        langsListView.setPrefSize(150,500);
        FlowPane root = new FlowPane(lineChart, langsListView,btn);
        root.setAlignment(Pos.CENTER);

        btn.setOnAction(event -> {
            langsListView = new ListView<>();
            numbers = new LinkedList<>();
            series = new XYChart.Series<>();
            i = 0;
            try {
                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return root;
    }

    private Parent getInformation() {
        Label label1 = new Label("Введите любое число");
        Kollat = new TextField("27");
        Kollat.setAlignment(Pos.CENTER);
        Label label = new Label("Показать разложение числа");
        Label label2 = new Label("по теореме Коллаца?");
        button = new Button("Yep!");
        Group group = new Group(button);
        FlowPane root = new FlowPane(label1,Kollat,label,label2, group);
        root.setAlignment(Pos.CENTER);
        root.setAlignment(Pos.BASELINE_CENTER);
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        scene = new Scene(getInformation(),183,130);
        primaryStage.setMaxHeight(130);
        primaryStage.setMinHeight(130);
        primaryStage.setMaxWidth(183);
        primaryStage.setMinWidth(183);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Гипотеза Коллатца in RealTime");
        button.setOnAction(event -> {
            if(check()>0) {
                scene = new Scene(graph(check()), 750, 550);
                primaryStage.setMaxHeight(570);
                primaryStage.setMinHeight(570);
                primaryStage.setMaxWidth(750);
                primaryStage.setMinWidth(750);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
    }
}
