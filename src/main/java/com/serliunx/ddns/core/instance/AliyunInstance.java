package com.serliunx.ddns.core.instance;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.alidns20150109.AsyncClient;
import com.aliyun.sdk.service.alidns20150109.models.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.serliunx.ddns.support.ConfigurationContextHolder;
import darabonba.core.client.ClientOverrideConfiguration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.serliunx.ddns.constant.ConfigurationKeys.KEY_ALIYUN_ENDPOINT;
import static com.serliunx.ddns.constant.SystemConstants.XML_ROOT_INSTANCE_NAME;

/**
 * 阿里云实例定义
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/5/15
 */
@JacksonXmlRootElement(localName = XML_ROOT_INSTANCE_NAME)
public class AliyunInstance extends AbstractInstance {

    /**
     * AccessKey ID
     */
    private String accessKeyId;

    /**
     * AccessKey Secret
     */
    private String accessKeySecret;

    /**
     * 解析记录ID
     */
    private String recordId;

    /**
     * 主机记录。
     * 如果要解析@.example.com，主机记录要填写”@”，而不是空。
     * 示例值:
     * www
     */
    private String rr;

    /**
     * 解析记录类型
     * <li>A记录	    A	参考标准；RR值可为空，即@解析；不允许含有下划线；	IPv4地址格式
     * <li>NS记录	NS	参考标准；RR值不能为空；允许含有下划线；不支持泛解析	NameType形式
     * <li>MX记录	MX	参考标准；RR值可为空，即@解析；不允许含有下划线	NameType形式，且不可为IP地址。1-10，优先级依次递减。
     * <li>TXT记录	TXT	参考标准；另外，有效字符除字母、数字、“-”（中横杠）、还包括“_”（下划线）；RR值可为空，即@解析；允许含有下划线；
     * 不支持泛解析	字符串；长度小于512,合法字符：大小写字母，数字,空格，及以下字符：-~=:;/.@+^!*
     * <li>CNAME记录	CNAME	参考标准；另外，有效字符除字母、数字、“-”（中横杠）、还包括“_”（下划线）；RR值不允许为空（即@）；
     * 允许含有下划线	NameType形式，且不可为IP
     * <li>SRV记录	SRV	是一个name，且可含有下划线“_“和点“.”；允许含有下划线；可为空（即@）；不支持泛解析	priority：优先级，
     * 为0－65535之间的数字；weight：权重，为0－65535之间的数字；port：提供服务的端口号，为0－65535之间的数字 target：为提供服务的目标地址，
     * 为nameType，且存在。参考：<a href="https://en.wikipedia.org/wiki/SRV_record">...</a>
     * <a href="http://www.rfc-editor.org/rfc/rfc2782.txt">...</a>
     * <li>AAAA记录	AAAA	参考标准；RR值可为空，即@解析；不允许含有下划线；	IPv6地址格式
     * <li>CAA记录	CAA	参考标准；RR值可为空，即@解析；不允许含有下划线；	格式为：[flag] [tag] [value]，是由一个标志字节的[flag],
     * 和一个被称为属性的标签[tag]-值[value]对组成。例如：@ 0 issue "symantec.com"或@ 0 iodef "mailto:admin@aliyun.com"
     * <li>显性URL转发	REDIRECT_URL	参考标准；RR值可为空，即@解析	NameType或URL地址（区分大小写），长度最长为500字符，
     * 其中域名，如example.com，必须，大小写不敏感；协议：可选，如HTTP、HTTPS，默认为HTTP端口：可选，如81，默认为80；路径：可选，大小写敏感，
     * 如/path/to/，默认为/；文件名：可选，大小写敏感，如file.php，默认无；参数：可选，大小写敏感，如?user=my***，默认无。
     * <li>隐性URL转发	FORWARD_URL	参考标准；RR值可为空，即@解析	NameType或URL地址（区分大小写），长度最长为500字符，其中域名，
     * 如example.com，必须，大小写不敏感；协议：可选，如HTTP、HTTPS，默认为HTTP端口：可选，如81，默认为80；路径：可选，大小写敏感，
     * 如/path/to/，默认为/；文件名：可选，大小写敏感，如file.php，默认无；参数：可选，大小写敏感，如?user=my***，默认无。
     */
    private String recordType;

    @JsonIgnore
    private AsyncClient client;

    @JsonIgnore
    private JsonMapper jsonMapper;

    @Override
    protected void init() {
        jsonMapper = new JsonMapper();

        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());
        client = AsyncClient.builder()
                .region("cn-hangzhou")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(ConfigurationContextHolder.getConfiguration().getString(KEY_ALIYUN_ENDPOINT))
                )
                .build();
		log.debug("{}: 初始化完成.", getName());
    }

    @Override
    protected void run0() {
        UpdateDomainRecordRequest request = UpdateDomainRecordRequest.builder()
                .recordId(recordId)
                .rr(rr)
                .type(recordType)
                .value(value)
                .build();
        log.debug("正在更新解析记录.");
        CompletableFuture<UpdateDomainRecordResponse> requestResponse = client.updateDomainRecord(request);
        try {
            requestResponse.whenComplete((v, t) -> {
                if (t != null) { //出现异常
                    log.error("出现异常 {} : {}", t.getCause(), t.getMessage());
                } else {
                    String result = null;
                    try {
                        result = jsonMapper.writeValueAsString(v.getBody());
                    } catch (JsonProcessingException ignored) {} finally {
                        log.debug("操作结束, 结果: {}", result == null ? v : result);
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String query() {
        DescribeDomainRecordInfoRequest describeDomainRecordInfoRequest = DescribeDomainRecordInfoRequest.builder()
                .recordId(recordId)
                .build();
        CompletableFuture<DescribeDomainRecordInfoResponse> responseCompletableFuture =
                client.describeDomainRecordInfo(describeDomainRecordInfoRequest);
        try {
            DescribeDomainRecordInfoResponse response = responseCompletableFuture.get(5, TimeUnit.SECONDS);
            DescribeDomainRecordInfoResponseBody body = response.getBody();
            if (body != null) {
                return body.getValue();
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            log.error("出现了不应该出现的异常 => {}", e.getMessage());
            return null;
        } catch (TimeoutException e) {
            log.error("记录查询超时!");
            return null;
        }
    }

    @Override
    protected boolean validate0() {
        //简单的必填参数校验
        return accessKeyId != null && !accessKeyId.isEmpty() && accessKeySecret != null && !accessKeySecret.isEmpty()
                && recordId != null && !recordId.isEmpty() && rr != null && !rr.isEmpty() && recordType != null;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRr() {
        return rr;
    }

    public void setRr(String rr) {
        this.rr = rr;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public AsyncClient getClient() {
        return client;
    }

    public void setClient(AsyncClient client) {
        this.client = client;
    }

    public JsonMapper getJsonMapper() {
        return jsonMapper;
    }

    public void setJsonMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public String toString() {
        return "AliyunInstance{" +
                "accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", recordId='" + recordId + '\'' +
                ", rr='" + rr + '\'' +
                ", recordType='" + recordType + '\'' +
                ", client=" + client +
                ", jsonMapper=" + jsonMapper +
                ", name='" + name + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", interval=" + interval +
                ", type=" + type +
                ", source=" + source +
                ", value='" + value + '\'' +
                '}';
    }
}
