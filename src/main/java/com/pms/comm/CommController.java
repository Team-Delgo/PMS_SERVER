package com.pms.comm;

import com.pms.comm.exception.ApiCode;
import com.pms.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;

public class CommController {

    public ResponseEntity SuccessReturn(Object data) {
        return ResponseEntity.ok().body(
                ResponseDTO.builder().code(ApiCode.SUCCESS.getCode()).codeMsg(ApiCode.SUCCESS.getMsg()).data(data).build());
    }

    public ResponseEntity SuccessReturn() {
        return ResponseEntity.ok().body(
                ResponseDTO.builder().code(ApiCode.SUCCESS.getCode()).codeMsg(ApiCode.SUCCESS.getMsg()).build());
    }

    public ResponseEntity ErrorReturn(ApiCode apiCode) {
        return ResponseEntity.ok().body(
                ResponseDTO.builder().code(apiCode.getCode()).codeMsg(apiCode.getMsg()).build());
    }
}
