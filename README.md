# 项目描述  
一个简易的扫码签到系统。在教师端生成二维码，二维码每五秒钟刷新一次，防止截图代签。学生在手机中扫描二维码并向后端发送请求，并在数据库中保存签到信息。
# 实现方法
使用了YXING库生成二维码以及调用摄像头权限。当用户扫描二维码时向后端发送请求，后端处理请求。
