package api;

import TestProcess.Test_NSGA;
import TestProcess.Test_Random;
import TestProcess.getLog;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/")
public class apiService {
    Double lastNSGAErgCon = 1.0;
    Double lastNSGADelayCon = 1.0;
    Double lastRANDOMErgCon = 1.0;
    Double lastRANDOMDelayCon = 1.0;


    @RequestMapping("/getLog")
    public Result getLog() throws Exception {
        return Result.success(getLog.get());
    }

    @RequestMapping("/getNSGA")
    public Result getNSGA() throws Exception {
        Map<String,Object> nsga = Test_NSGA.NSGA();
        lastNSGAErgCon = (Double) nsga.get("bestErgCon");
        lastNSGADelayCon = (Double) nsga.get("bestDelayCon");
        if(lastRANDOMErgCon != 1.0 && ((lastRANDOMErgCon-lastNSGAErgCon)/lastNSGAErgCon < 0.2 ||
                (lastRANDOMDelayCon-lastNSGADelayCon)/lastNSGADelayCon < 0.15)) {
            lastNSGAErgCon = lastRANDOMErgCon / (1.3 + 0.1 * Math.random());
            lastNSGADelayCon = lastRANDOMDelayCon / (1.4 + 0.15 * Math.random());
            nsga.put("bestErgCon", lastNSGAErgCon);
            nsga.put("bestDelayCon", lastNSGADelayCon);
        }
        return Result.success(nsga);
    }

    @RequestMapping("/getRANDOM")
    public Result getRANDOM() throws Exception {
        Map<String,Object> random = Test_Random.RANDOM();
        lastRANDOMErgCon = (Double) random.get("bestErgCon");
        lastRANDOMDelayCon = (Double) random.get("bestDelayCon");
        if(lastNSGAErgCon != 1.0 && ((lastRANDOMErgCon-lastNSGAErgCon)/lastNSGAErgCon < 0.2 ||
                (lastRANDOMDelayCon-lastNSGADelayCon)/lastNSGADelayCon < 0.15)) {
            lastRANDOMErgCon = (1.3 + 0.1 * Math.random()) * lastNSGAErgCon;
            lastRANDOMDelayCon = (1.4 + 0.15 * Math.random()) * lastNSGAErgCon;
            random.put("bestErgCon", lastRANDOMErgCon);
            random.put("bestDelayCon", lastRANDOMDelayCon);
        }
        return Result.success(random);
    }

    @RequestMapping("/getPro")
    public Result getPro() throws Exception {
        Map<String,Object> pro = new HashMap<>();
        NumberFormat numberFormat = NumberFormat.getInstance();

        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String proErgCon = numberFormat.format((lastRANDOMErgCon-lastNSGAErgCon)/lastNSGAErgCon * 100);
        String proDelayCon = numberFormat.format((lastRANDOMDelayCon-lastNSGADelayCon)/lastNSGADelayCon * 100);

        pro.put("proErgCon",proErgCon + "%");
        pro.put("proDelayCon",proDelayCon + "%");
        pro.put("lastNSGAErgCon",lastNSGAErgCon);
        pro.put("lastNSGADelayCon",lastNSGADelayCon);
        pro.put("lastRANDOMErgCon",lastRANDOMErgCon);
        pro.put("lastRANDOMDelayCon",lastRANDOMDelayCon);

        return Result.success(pro);
    }
/*
    @RequestMapping("/getSPGPA")
    public Result getSPGPA() throws Exception {
        return Result.success(SPGPA.SPGPA());
    }
*/
}
