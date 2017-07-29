# apache-comnons系列之commons-email1.4 学习笔记
## 概述
### Email
- The base class for all email messages.  This class sets the
- sender's email &amp; name, receiver's email &amp; name, subject, and the sent date.
- Subclasses are responsible for setting the message body.
#### SimpleEmail
- This class is used to send simple internet email messages without attachments.
#### MultiPartEmail
- A multipart email.This class is used to send multi-part internet email like messages with attachments.
- To create a multi-part email, call the default constructor and then you can call setMsg() to set the message and call the different attach() methods.
#### EmailAttachment
- This class models an email attachment. Used by MultiPartEmail.
##### HtmlEmail
- An HTML multipart email.
- This class is used to send HTML formatted email.  A text message can also be set for HTML unaware email clients, such as text-based email clients.
##### ImageHtmlEmail
- Small wrapper class on top of HtmlEmail which encapsulates the required logic to retrieve images that are contained in "&lt;img src=../&gt;" elements in the HTML
- code. This is done by replacing all img-src-elements with "cid:"-entries and embedding images in the email.
#### EmailUtils
- Utility methods used by commons-email.
## 测试
- org.apache.commons.mail.EmailTest
    - MimeMessage message----The email message to send.
    - Object content----The content.
    - MimeMultipart emailBody----An attachment
    - send
      - Sends the email. Internally we build a MimeMessage which is afterwards sent to the SMTP server.
    - buildMimeMessage
      - Does the work of actually building the MimeMessage. Please note that a user rarely calls this method directly and only if he/she is interested in the sending the underlying MimeMessage without commons-email.
      - this.message = this.createMimeMessage(this.getMailSession());
      - if (this.content != null)----else if (this.emailBody != null)
    - sendMimeMessage
      - Sends the previously created MimeMessage to the SMTP server.
      - Transport.send(this.message);
- org.apache.commons.mail.MultiPartEmailTest
    - MimeMultipart container----Body portion of the email
    - BodyPart primaryBodyPart---The message container.
    - boolean initialized
    - init
      - Initialize the multipart email.
      - container = createMimeMultipart();
      - super.setContent(container);
        -  this.emailBody = aMimeMultipart;
    - setMsg
      - Set the message of the email.
      - final BodyPart primary = getPrimaryBodyPart();
    - getPrimaryBodyPart
      - primaryBodyPart = createBodyPart();
      - getContainer().addBodyPart(primaryBodyPart, 0);
    - attach
      - Attach a file.
      - final BodyPart bodyPart = createBodyPart();
      - bodyPart.setDataHandler(new DataHandler(ds));
      - getContainer().addBodyPart(bodyPart);
    - buildMimeMessage
      - Does the work of actually building the MimeMessage. Please note that a user rarely calls this method directly and only if he/she is interested in the sending the underlying MimeMessage without commons-email.
      - final BodyPart body = this.getPrimaryBodyPart();
      - super.buildMimeMessage();----这里与init时候的super.setContent(container);呼应上
- org.apache.commons.mail.HtmlEmailTest
    - String text
      - Text part of the message. This will be used as alternative text if the email client does not support HTML messages.
    - String html
      - Html part of the message.
    - inlineEmbeds = new HashMap String, InlineImage ();
      - Embedded images Map where the key is the user-defined image name.
    - embed
      - Attempts to parse the specified <code>String</code> as a URL that will then be embedded in the message.
      - final MimeBodyPart mbp = new MimeBodyPart();
      - mbp.setDataHandler(new DataHandler(dataSource));
      - final InlineImage ii = new InlineImage(encodedCid, dataSource, mbp);
      - this.inlineEmbeds.put(name, ii);
    - build
      - bodyContainer.addBodyPart(msgHtml, 0);
      - bodyEmbedsContainer.addBodyPart(image.getMbp());
      - bodyContainer.addBodyPart(msgText, 0);
- org.apache.commons.mail.ImageHtmlEmailTest
    - REGEX_IMG_SRC----Regexp for extracting  tags
    - REGEX_SCRIPT_SRC--regexp for extracting  tags
    -  String temp = replacePattern(super.html, IMG_PATTERN);
    - temp = replacePattern(temp, SCRIPT_PATTERN);
    - setHtmlMsg(temp);