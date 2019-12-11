import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


import java.util.Collection;
import java.util.Collections;

import static javafx.application.Application.launch;

public class Main extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
//        MathModel1 mm1 = new MathModel1(-1.5, 1.5, 0.1);
//        Integrator runge4 = new Integrator();
//        mm1.x0 = new Vector(new double[]{0.01});
//        runge4.run(mm1);
//        mm1.result.show();
        long start = System.currentTimeMillis();
        ArenstorfModel arenstorfModel = new ArenstorfModel(0, 90 , 0.01);
        DormanPrinceIntegrator runge4 = new DormanPrinceIntegrator();
        arenstorfModel.x0 = new Vector(new double[] {0.994, 0, 0, -2.0317326295573268357302057924});
        runge4.run(arenstorfModel);
        long end = System.currentTimeMillis() - start;
//        arenstorfModel.result.show();
//        Kamen kamen1 = new Kamen(0, 5 , 0.001);
//        DormanPrinceIntegrator runge = new DormanPrinceIntegrator();
//        runge.run(kamen1);


        NumberAxis xAxis = new NumberAxis(-2, 2, 1);
        NumberAxis yAxis = new NumberAxis(-2, 2, 1);
        xAxis.setLabel("Y1");
        yAxis.setLabel("Y2");


        LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        lineChart.setTitle("Graphic");
        lineChart.setPrefHeight(700);
        lineChart.setPrefWidth(900);
        lineChart.setCreateSymbols(false);
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < arenstorfModel.result.data[0].length; i++) {
            series.getData().add(new XYChart.Data(arenstorfModel.result.data[0][i],
                    arenstorfModel.result.data[2][i]));
        }

     //   double maxh = Collections.max(runge4.list);
     //   double minh = Collections.min(runge4.list);
        lineChart.getData().addAll(series);

        Group root = new Group(lineChart);

        //Creating a scene object
        Scene scene = new Scene(root, 1000  , 800);

        //Setting title to the Stage
        primaryStage.setTitle("Line Chart");

        //Adding scene to the stage
        primaryStage.setScene(scene);

        //Displaying the contents of the stage
        primaryStage.show();
    }
}
