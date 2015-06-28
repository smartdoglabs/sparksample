import spark.Route;

/**
 * Created by joserubio on 6/28/15.
 */
public class LambdaRoute {

    public static Route lambdaRoute(){
        return (req,res) -> "Lambda passing";
    }
}
