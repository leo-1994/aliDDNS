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

    private static final String regionId = "cn-hangzhou";

    private static String oldIp = "";

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("参数错误");
            return;
        }
        // aliyun 配置
        String accessKeyId = args[0];
        String accessKeySecret = args[1];
        String rootDomain = args[2];
        String[] subdomainArray = args[3].split(",");

        while (true) {
            execute(accessKeyId, accessKeySecret, rootDomain, subdomainArray);
            try {
                Thread.sleep(60 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static void execute(String accessKeyId, String accessKeySecret, String rootDomain, String[] subdomainArray) {
        String ip;
        try {
            ip = IPUtil.getPublicIp();
        } catch (IOException e) {
            System.out.println("获取公网ip失败,error" + e.getMessage());
            return;
        }
        if (ip == null || ip.length() == 0) {
            System.out.println("获取公网ip失败");
            return;
        }

        if (oldIp.equals(ip)) {
            System.out.println("ip未变动");
            return;
        } else {
            oldIp = ip;
        }

        AliDNSOption option = new AliDNSOption(regionId, accessKeyId, accessKeySecret);
        // 获取解析记录列表
        List<DescribeDomainRecordsResponse.Record> recordList;
        try {
            recordList = option.getRecordList(rootDomain);
        } catch (ClientException e) {
            System.out.println("获取解析记录列表失败,error:" + e.getMessage());
            return;
        }

        List<String> subdomains = Arrays.asList(subdomainArray);
        for (DescribeDomainRecordsResponse.Record record : recordList) {
            if (subdomains.contains(record.getRR())) {
                if (ip.equals(record.getValue())) {
                    // 如果ip与旧的解析值相同，则跳过
                    continue;
                }
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
