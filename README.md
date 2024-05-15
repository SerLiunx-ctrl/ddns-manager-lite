# DDNS Manager Lite

## 简介
这是一个动态域名解析（Dynamic DNS，简称 DDNS）工具，用于自动更新域名解析记录，以适应动态 IP 地址变化的情况。 当然，这是一个简化版本

## 功能特性
- 支持阿里云 DNS 解析
- 支持腾讯云 DNS 解析(开发中)
- 实例可继承, 用于填写公共参数
- 自动检测并更新域名解析记录
- 配置简单，使用方便
- 计划支持多种数据存储方式（JSON、XML、数据库等）
- 多线程并发处理

## 环境要求
- jre 1.8 或以上版本
- 操作系统: 任何能运行jre 1.8的操作系统

## 如何使用
1. 下载最新版本的 DDNS 工具 [Release 页面](https://github.com/your-repo/ddns-tool/releases)
2. 配置 `application.yml` 文件, 配置日志、线程相关信息
3. 按照下方实例配置进行实例的创建, 目前支持`.json`、`.xml`、`.yaml/.yml`的文件
4. 解压压缩包到你的计算机上, 运行`start.bat` (如果运行失败, 请检查你的Java是否已安装并成功设置环境变量, 或者编辑该文件手动设置Java信息)
5. 工具会定时检测 IP 地址变化，并自动更新域名解析记录

## 阿里云示例配置
```jsmin
//父实例, 注意: 每个文件只能有一个实例信息
{
  "type": "TENCENT_CLOUD",  //实例类型
  "interval": 300,                  //执行周期(单位秒)
  "name": "root-instance"           //实例名称
}
```

```jsmin
//子实例
{
  "type": "ALI_YUN",                                //实例类型
  "name": "serliunx-aliyun",                                //实例名称
  "accessKeyId": "xxxxxxxxxxxxxxxxxx",                      //实例不同, 参数也不同: 阿里云的AKID
  "accessKeySecret": "yyyyyyyyyyyyyyy",                     //实例不同, 参数也不同: 阿里云的AKS
  "recordId": "889741081843109888",                         //实例不同, 参数也不同: 阿里云解析记录的ID
  "rr": "main",                                             //主机记录, 如域名serliunx.com -> main.serliunx.com
  "type": "A",                                              //记录类型, 详见阿里云的相关文档
  "fatherName": "root-instance"                     //父实例名称, 这里指定了上方的实例, 继承了interval属性.
}
```

## 腾讯云示例配置
* 暂无

## 注意事项
* 请确保 DNS 解析商的 API 权限已经正确配置，具有修改域名解析记录的权限
* 配置文件中的敏感信息请妥善保管，不要泄露给他人

## 开源许可
本工具基于 `GPL 3.0` 许可 进行开源，详情请参阅 LICENSE 文件。

## 感谢
- 感谢所有为开源事业做出贡献的开发者们，感谢你们的无私奉献和辛勤努力。