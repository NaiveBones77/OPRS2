import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.*;
import javafx.stage.Stage;

public class Controller {
    double y0 = 1116 / 2;
    double x0 = 571  / 2;
    GraphicsContext gc;

    @FXML
    private Canvas cvs;

    @FXML
    void Draw(ActionEvent event) throws Exception {
        MathModel1 mm1 = new MathModel1(-1.5, 1.5, 0.01);
        Integrator runge4 = new Integrator();
        mm1.x0 = new Vector(new double[]{0.01});
        runge4.run(mm1);
        mm1.result.show();

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("t");
        yAxis.setLabel("Y");

        LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Graphic");
        XYChart.Series series = new XYChart.Series();

        double t = mm1.t0;
        for (int i = 0; i < mm1.result.data[0].length; i++) {
            series.getData().add(new XYChart.Data(t , mm1.result.data[0][i]));
            t+=mm1.h;
        }

        lineChart.getData().add(series);
        Group root = new Group(lineChart);
        Scene scene = new Scene(root ,600, 300);
    }

}
