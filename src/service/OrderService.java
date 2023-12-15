package service;

import dao.*;
import model.*;
import utils.*;

import java.sql.*;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class OrderService {
    private OrderDao oDao = new OrderDao();
    private UserDao uDao = new UserDao();
    public void addOrder(Order order) {
        Connection con = null;
        try {
            con = DataSourceUtils.getConnection();
            con.setAutoCommit(false);

            oDao.insertOrder(con, order);
            int id = oDao.getLastInsertId(con);
            order.setId(id);
            for(OrderItem item : order.getItemMap().values()) {
                oDao.insertOrderItem(con, item);
            }

            con.commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if(con!=null)
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
        }
    }
    public List<Order> selectAll(int userid){
        List<Order> list=null;
        try {
            list = oDao.selectAll(userid);
            for(Order o :list) {
                List<OrderItem> l = oDao.selectAllItem(o.getId());
                o.setItemList(l);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
    public Page getOrderPage(int status,int pageNumber) {
        Page p = new Page();
        p.setPageNumber(pageNumber);
        int pageSize = 10;
        int totalCount = 0;
        try {
            totalCount = oDao.getOrderCount(status);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.SetPageSizeAndTotalCount(pageSize, totalCount);
        List list=null;
        try {
            list = oDao.selectOrderList(status, pageNumber, pageSize);
            for(Order o :(List<Order>)list) {
                List<OrderItem> l = oDao.selectAllItem(o.getId());
                o.setItemList(l);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.setList(list);
        return p;
    }
    public void updateStatus(int id,int status) {
        try {
            oDao.updateStatus(id, status);
            if (status == 3) {
                sendEmailToUser(id);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void sendEmailToUser(int orderId) {
        try {
            // 获取订单信息
            int user_id = oDao.getUserIdByOrderId(orderId);
            System.out.println("test1:"+user_id);
            // 获取用户信息
            User user = uDao.selectById(user_id);
            System.out.println("test2:"+user.getId());


            // 设置电子邮件属性
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.qq.com"); // QQ 邮箱的 SMTP 服务器地址
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // 设置邮箱账号和密码
            final String username = "djcy114514@qq.com";
            final String password = "jvadikgelmgwdaec";

            // 创建会话
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // 创建邮件消息
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("订单发货通知");
            // 创建一个包含文本和可能附件的复合消息
            MimeMultipart multipart = new MimeMultipart();

            // 添加 HTML 部分
            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = "<html>"
                    + "<head>"
                    + "<style>"
                    + "body {"
                    + "    background-color: #f4f4f4;"
                    + "    font-family: 'Arial', sans-serif;"
                    + "}"
                    + "p {"
                    + "    color: #333;"
                    + "}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<p>尊敬的" + user.getName() + "：</p>"
                    + "<p style='color: #007bff;'>您在手办商场的订单已成功发货，订单编号：" + orderId + "。</p>"
                    + "<p>感谢您的购物，祝您生活愉快！</p>"
                    + "</body>"
                    + "</html>";
            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);

            // 将复合消息设置为邮件内容
            message.setContent(multipart);

            // 发送邮件
            Transport.send(message);
            System.out.println("邮件已发送给用户：" + user.getEmail());

        } catch (AuthenticationFailedException e) {
            System.err.println("电子邮件认证失败。请检查您的用户名和密码。");
            e.printStackTrace();
        } catch (MessagingException e) {
            System.err.println("发送电子邮件时发生错误。请检查您的电子邮件设置并重试。");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("发生意外错误。");
            e.printStackTrace();
        }
    }
    public void delete(int id) {
        Connection con = null;
        try {
            con = DataSourceUtils.getDataSource().getConnection();
            con.setAutoCommit(false);

            oDao.deleteOrderItem(con, id);
            oDao.deleteOrder(con, id);
            con.commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if(con!=null)
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
        }


    }
}
