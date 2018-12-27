package com.leo.alidnns;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.List;

/**
 * 阿里云dns sdk操作类
 *
 * @author chao.li@quvideo.com
 * @date 2018/12/27 17:15
 */
public class AliDNSOption {

    private IAcsClient client;

    public AliDNSOption(String regionId, String accessKeyId, String accessKeySecret) {
        this.client = getClient(regionId, accessKeyId, accessKeySecret);
    }

    /**
     * 获取client
     *
     * @param regionId
     * @param accessKeyId
     * @param accessKeySecret
     * @return IAcsClient
     */
    private IAcsClient getClient(String regionId, String accessKeyId, String accessKeySecret) {
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        return new DefaultAcsClient(profile);
    }

    /**
     * 获取域名解析记录列表
     *
     * @param rootDomain 根域名
     * @return
     */
    public List<DescribeDomainRecordsResponse.Record> getRecordList(String rootDomain) throws ClientException {
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        request.setDomainName(rootDomain);
        DescribeDomainRecordsResponse response = this.client.getAcsResponse(request);
        return response.getDomainRecords();
    }

    /**
     * 更新域名解析记录
     * @param ip
     * @param recordId
     * @param rr
     * @throws ClientException
     */
    public void updateDomainRecord(String ip, String recordId, String rr) throws ClientException {
        // 更新
        UpdateDomainRecordRequest updateRequest = new UpdateDomainRecordRequest();
        // 初始化更新域名解析的类
        updateRequest.setType("A");
        // 设置新的 IP
        updateRequest.setValue(ip);
        // recordId
        updateRequest.setRecordId(recordId);
        updateRequest.setRR(rr);
        this.client.getAcsResponse(updateRequest);
    }
}
