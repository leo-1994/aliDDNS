package com.leo.alidnns;

import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.exceptions.ClientException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author chao.li@quvideo.com
 * @date 2018/12/27 16:27
 */
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            System.out.println("参数错误");
            return;
        }
        // aliyun 配置
        String accessKeyId = args[0];
        String accessKeySecret = args[1];
        String rootDomain = args[2];
        String[] subdomainArray = args[3].split(",");
        String regionId = "cn-hangzhou";

        String ip = IPUtil.getPublicIp();
        if (ip == null || ip.length() == 0) {
            System.out.println("获取公网ip失败");
            return;
        }

        AliDNSOption option = new AliDNSOption(regionId, accessKeyId, accessKeySecret);
        // 获取解析记录列表
        List<DescribeDomainRecordsResponse.Record> recordList;
        try {
            recordList = option.getRecordList(rootDomain);
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("获取解析记录列表失败");
            return;
        }

        List<String> subdomains = Arrays.asList(subdomainArray);
        for (DescribeDomainRecordsResponse.Record record : recordList) {
            if (subdomains.contains(record.getRR())) {
                try {
                    option.updateDomainRecord(ip, record.getRecordId(), record.getRR());
                    System.out.println(record.getRR() + "更新域名解析成功");
                } catch (ClientException e) {
                    System.out.println(record.getRR() + "更新域名解析失败，error:" + e.getMessage());
                }
            }
        }

    }

}
