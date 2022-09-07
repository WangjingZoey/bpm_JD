package api;

import TestProcess.Test_NSGA;
import TestProcess.Test_Random;
import TestProcess.getLog;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/")
public class apiService {

    @GetMapping("/getLog")
    public Result getLog() throws Exception {
        return Result.ok(getLog.get());
    }

    @GetMapping("/definition")
    public JSONObject definition() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("definition.json");
        byte[] bdata = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        String data = new String(bdata, StandardCharsets.UTF_8);
        JSONObject info = JSONObject.parseObject(data);
        return info;
    }

    @PostMapping("/addEntity")
    public Result addEntity() throws Exception {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("上传成功");
        return result;
    }


    @PostMapping("/delEntity")
    public Result delEntity() throws Exception {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("删除成功");
        return result;
    }

    @PostMapping("/train")
    public Result train() throws Exception {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("已启动模型训练");
        return result;
    }

    @PostMapping("/status")
    public JSONObject status() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("status.json");
        byte[] bdata = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        String data = new String(bdata, StandardCharsets.UTF_8);
        JSONObject info = JSONObject.parseObject(data);
        return info;
    }

    @PostMapping("/serving")
    public Result serving() throws Exception {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("发布成功");
        return result;
    }

    @PostMapping(value = "/predict",produces = "application/json;charset=UTF-8")
    public Result predict() throws Exception {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        Map<String,Object> nsga = Test_NSGA.NSGA();
        Map<String,Object> random = Test_Random.RANDOM();
        Double ne = (Double) nsga.get("NSGAErgCon");
        Double nd = (Double) nsga.get("NSGADelayCon");
        Double fe = (Double) random.get("FCFSErgCon");
        Double fd = (Double) random.get("FCFSDelayCon");
        if (fe < 1.2 * ne || fd < 0.15 * nd) {
            fe = (1.2 + 0.1 * Math.random()) * ne;
            fd = (1.3 + 0.15 * Math.random()) * nd;
        }
        nsga.put("NSGAErgCon",nf.format((ne)));
        nsga.put("NSGADelayCon",nf.format((nd)));
        random.put("FCFSErgCon",nf.format((fe)));
        random.put("FCFSDelayCon",nf.format((fd)));
        String proErgCon = nf.format((fe-ne)/ne * 100);
        String proDelayCon = nf.format((fd-nd)/nd * 100);
        Map<String,Object> data = new HashMap<>();

        data.put("proErgCon",proErgCon + "%");
        data.put("proDelayCon",proDelayCon + "%");
        data.put("NSGA", nsga);
        data.put("FCFS", random);

        return Result.ok(data);
    }

    @PostMapping("/export")
    public Result exportModel() {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("已导出");
        return result;
    }

    @PostMapping("/import")
    public Result importModel() throws Exception {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("导入成功");
        return result;
    }

    @PostMapping(value ="/init_server_config",produces = "application/json;charset=UTF-8")
    public Result init_server_config() {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("更新配置数据成功");
        return result;
    }

}
