package neu.homework.sunshine.ums.feign;

import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient("third-party")
public interface FtpClient {

    @PostMapping(value = "/ftp/feign/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = {"application/json"})
    public Result upload(@RequestPart("file") MultipartFile multipartFile,
                         @RequestParam("dir") String dir,
                         @RequestParam("fileName") String fileName) throws ProcessException;

    @DeleteMapping("/ftp/feign/delete")
    public Result delete(@RequestParam("dir") String dir,
                         @RequestParam("fileName") String fileName) throws ProcessException;
}
