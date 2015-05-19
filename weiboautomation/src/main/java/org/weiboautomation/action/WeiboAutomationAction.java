package org.weiboautomation.action;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weiboautomation.action.exception.ActionException;
import org.weiboautomation.entity.Blog;
import org.weiboautomation.entity.CollectingUser;
import org.weiboautomation.entity.FillingUserOperator;
import org.weiboautomation.entity.FollowingUserOperator;
import org.weiboautomation.entity.GloballyAddingCommentOperator;
import org.weiboautomation.entity.IndividuallyAddingCommentOperator;
import org.weiboautomation.entity.IndividuallyAddingMessageOperator;
import org.weiboautomation.entity.PpTidType;
import org.weiboautomation.entity.PublishingBlogOperator;
import org.weiboautomation.entity.PublishingMicroTaskOperator;
import org.weiboautomation.entity.QueryingUserOperator;
import org.weiboautomation.entity.SaeStorage;
import org.weiboautomation.entity.GloballyAddingMessageOperator;
import org.weiboautomation.entity.TiminglyPublishingBlogOperator;
import org.weiboautomation.entity.TransferingBlogOperator;
import org.weiboautomation.entity.TransferingUserOperator;
import org.weiboautomation.entity.Type;
import org.weiboautomation.entity.User;
import org.weiboautomation.entity.UserBase;
import org.weiboautomation.entity.UserPhase;
import org.weiboautomation.entity.UserProfile;
import org.weiboautomation.entity.MicroTask;
import org.weiboautomation.handler.PpHandler;
import org.weiboautomation.handler.SaeAppBatchhelperHandler;
import org.weiboautomation.handler.SaeStorageHandler;
import org.weiboautomation.handler.VdiskHandler;
import org.weiboautomation.handler.WeiboApiHandler;
import org.weiboautomation.handler.WeiboHandler;
import org.weiboautomation.handler.MicroTaskHandler;
import org.weiboautomation.handler.exception.HandlerException;
import org.weiboautomation.service.BlogService;
import org.weiboautomation.service.CollectingUserService;
import org.weiboautomation.service.FillingUserOperatorService;
import org.weiboautomation.service.FollowedUserService;
import org.weiboautomation.service.FollowingUserOperatorService;
import org.weiboautomation.service.GloballyAddingCommentOperatorService;
import org.weiboautomation.service.IndividuallyAddingCommentOperatorService;
import org.weiboautomation.service.IndividuallyAddingMessageOperatorService;
import org.weiboautomation.service.PpTidTypeService;
import org.weiboautomation.service.PublishingBlogOperatorService;
import org.weiboautomation.service.PublishingMicroTaskOperatorService;
import org.weiboautomation.service.QueryingUserOperatorService;
import org.weiboautomation.service.GloballyAddingMessageOperatorService;
import org.weiboautomation.service.TiminglyPublishingBlogOperatorService;
import org.weiboautomation.service.TransferingBlogOperatorService;
import org.weiboautomation.service.TransferingUserOperatorService;
import org.weiboautomation.service.TypeService;
import org.weiboautomation.service.UserBaseService;
import org.weiboautomation.service.UserProfileService;
import org.weiboautomation.service.UserService;
import org.weiboautomation.service.exception.ServiceException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeiboAutomationAction {

	private static final Logger logger = LoggerFactory
			.getLogger(WeiboAutomationAction.class);

	private PpHandler ppHandler;

	private String ppUsername;

	private String ppPassword;

	private PpTidTypeService ppTidTypeService;

	private BlogService blogService;

	private TransferingBlogOperatorService transferingBlogOperatorService;

	private int transferingBlogSize;

	private WeiboHandler weiboHandler;

	private TypeService typeService;

	private VdiskHandler vdiskHandler;

	private SaeStorageHandler saeStorageHandler;

	private String saeStorageAccessKey;

	private String saeStorageSecretKey;

	private int publishingBlogSize;

	private PublishingBlogOperatorService publishingBlogOperatorService;

	private SaeAppBatchhelperHandler saeAppBatchhelperHandler;

	private TiminglyPublishingBlogOperatorService timinglyPublishingBlogOperatorService;

	private PublishingMicroTaskOperatorService publishingMicroTaskOperatorService;

	private MicroTaskHandler microTaskHandler;

	private QueryingUserOperatorService queryingUserOperatorService;

	private CollectingUserService collectingUserService;

	private WeiboApiHandler weiboApiHandler;

	private UserService userService;

	private int filteringUserSize;

	private FollowingUserOperatorService followingUserOperatorService;

	private int followingUserSize;

	private FollowedUserService followedUserService;

	private int reservingFollowedDays;

	private FillingUserOperatorService fillingUserOperatorService;

	private int fillingUserSize;

	private UserProfileService userProfileService;

	private TransferingUserOperatorService transferingUserOperatorService;

	private int transferingUserSize;

	private ObjectMapper objectMapper;

	private UrlValidator urlValidator;

	private UserBaseService userBaseService;

	private GloballyAddingMessageOperatorService globallyAddingMessageOperatorService;

	private IndividuallyAddingMessageOperatorService individuallyAddingMessageOperatorService;

	private GloballyAddingCommentOperatorService globallyAddingCommentOperatorService;

	private IndividuallyAddingCommentOperatorService individuallyAddingCommentOperatorService;

	private DefaultHttpClient collectingBlogsDefaultHttpClient;

	private DefaultHttpClient transferingBlogsDefaultHttpClient;

	private DefaultHttpClient publishingBlogsDefaultHttpClient;

	private DefaultHttpClient timinglyPublishingBlogsDefaultHttpClient;

	private DefaultHttpClient publishingMicroTasksDefaultHttpClient;

	private DefaultHttpClient collectingUsersDefaultHttpClient;

	private DefaultHttpClient followingUsersGloballyDefaultHttpClient;

	private DefaultHttpClient unfollowingUsersGloballyDefaultHttpClient;

	private DefaultHttpClient followingUsersIndividuallyDefaultHttpClient;

	private DefaultHttpClient unfollowingUsersIndividuallyDefaultHttpClient;

	private DefaultHttpClient fillingUsersDefaultHttpClient;

	private DefaultHttpClient transferingUsersDefaultHttpClient;

	private DefaultHttpClient globallyAddingMessagesDefaultHttpClient;

	private DefaultHttpClient individuallyAddingMessagesDefaultHttpClient;

	private DefaultHttpClient globallyAddingCommentsDefaultHttpClient;

	private DefaultHttpClient individuallyAddingCommentsDefaultHttpClient;

	public void setPpHandler(PpHandler ppHandler) {
		this.ppHandler = ppHandler;
	}

	public void setPpUsername(String ppUsername) {
		this.ppUsername = ppUsername;
	}

	public void setPpPassword(String ppPassword) {
		this.ppPassword = ppPassword;
	}

	public void setPpTidTypeService(PpTidTypeService ppTidTypeService) {
		this.ppTidTypeService = ppTidTypeService;
	}

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

	public void setTransferingBlogOperatorService(
			TransferingBlogOperatorService transferingBlogOperatorService) {
		this.transferingBlogOperatorService = transferingBlogOperatorService;
	}

	public void setTransferingBlogSize(int transferingBlogSize) {
		this.transferingBlogSize = transferingBlogSize;
	}

	public void setWeiboHandler(WeiboHandler weiboHandler) {
		this.weiboHandler = weiboHandler;
	}

	public void setTypeService(TypeService typeService) {
		this.typeService = typeService;
	}

	public void setVdiskHandler(VdiskHandler vdiskHandler) {
		this.vdiskHandler = vdiskHandler;
	}

	public void setSaeStorageHandler(SaeStorageHandler saeStorageHandler) {
		this.saeStorageHandler = saeStorageHandler;
	}

	public void setSaeStorageAccessKey(String saeStorageAccessKey) {
		this.saeStorageAccessKey = saeStorageAccessKey;
	}

	public void setSaeStorageSecretKey(String saeStorageSecretKey) {
		this.saeStorageSecretKey = saeStorageSecretKey;
	}

	public void setPublishingBlogSize(int publishingBlogSize) {
		this.publishingBlogSize = publishingBlogSize;
	}

	public void setPublishingBlogOperatorService(
			PublishingBlogOperatorService publishingBlogOperatorService) {
		this.publishingBlogOperatorService = publishingBlogOperatorService;
	}

	public void setSaeAppBatchhelperHandler(
			SaeAppBatchhelperHandler saeAppBatchhelperHandler) {
		this.saeAppBatchhelperHandler = saeAppBatchhelperHandler;
	}

	public void setTiminglyPublishingBlogOperatorService(
			TiminglyPublishingBlogOperatorService timinglyPublishingBlogOperatorService) {
		this.timinglyPublishingBlogOperatorService = timinglyPublishingBlogOperatorService;
	}

	public void setPublishingMicroTaskOperatorService(
			PublishingMicroTaskOperatorService publishingMicroTaskOperatorService) {
		this.publishingMicroTaskOperatorService = publishingMicroTaskOperatorService;
	}

	public void setMicroTaskHandler(MicroTaskHandler microTaskHandler) {
		this.microTaskHandler = microTaskHandler;
	}

	public void setQueryingUserOperatorService(
			QueryingUserOperatorService queryingUserOperatorService) {
		this.queryingUserOperatorService = queryingUserOperatorService;
	}

	public void setCollectingUserService(
			CollectingUserService collectingUserService) {
		this.collectingUserService = collectingUserService;
	}

	public void setWeiboApiHandler(WeiboApiHandler weiboApiHandler) {
		this.weiboApiHandler = weiboApiHandler;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setFilteringUserSize(int filteringUserSize) {
		this.filteringUserSize = filteringUserSize;
	}

	public void setFollowingUserOperatorService(
			FollowingUserOperatorService followingUserOperatorService) {
		this.followingUserOperatorService = followingUserOperatorService;
	}

	public void setFollowingUserSize(int followingUserSize) {
		this.followingUserSize = followingUserSize;
	}

	public void setFollowedUserService(FollowedUserService followedUserService) {
		this.followedUserService = followedUserService;
	}

	public void setReservingFollowedDays(int reservingFollowedDays) {
		this.reservingFollowedDays = reservingFollowedDays;
	}

	public void setFillingUserOperatorService(
			FillingUserOperatorService fillingUserOperatorService) {
		this.fillingUserOperatorService = fillingUserOperatorService;
	}

	public void setFillingUserSize(int fillingUserSize) {
		this.fillingUserSize = fillingUserSize;
	}

	public void setUserProfileService(UserProfileService userProfileService) {
		this.userProfileService = userProfileService;
	}

	public void setTransferingUserOperatorService(
			TransferingUserOperatorService transferingUserOperatorService) {
		this.transferingUserOperatorService = transferingUserOperatorService;
	}

	public void setTransferingUserSize(int transferingUserSize) {
		this.transferingUserSize = transferingUserSize;
	}

	public void setUserBaseService(UserBaseService userBaseService) {
		this.userBaseService = userBaseService;
	}

	public void setGloballyAddingMessageOperatorService(
			GloballyAddingMessageOperatorService globallyAddingMessageOperatorService) {
		this.globallyAddingMessageOperatorService = globallyAddingMessageOperatorService;
	}

	public void setIndividuallyAddingMessageOperatorService(
			IndividuallyAddingMessageOperatorService individuallyAddingMessageOperatorService) {
		this.individuallyAddingMessageOperatorService = individuallyAddingMessageOperatorService;
	}

	public void setGloballyAddingCommentOperatorService(
			GloballyAddingCommentOperatorService globallyAddingCommentOperatorService) {
		this.globallyAddingCommentOperatorService = globallyAddingCommentOperatorService;
	}

	public void setIndividuallyAddingCommentOperatorService(
			IndividuallyAddingCommentOperatorService individuallyAddingCommentOperatorService) {
		this.individuallyAddingCommentOperatorService = individuallyAddingCommentOperatorService;
	}

	public void initialize() {
		objectMapper = new ObjectMapper();

		urlValidator = UrlValidator.getInstance();

		collectingBlogsDefaultHttpClient = getDefaultHttpClient();
		transferingBlogsDefaultHttpClient = getDefaultHttpClient();
		publishingBlogsDefaultHttpClient = getDefaultHttpClient();
		timinglyPublishingBlogsDefaultHttpClient = getDefaultHttpClient();
		publishingMicroTasksDefaultHttpClient = getDefaultHttpClient();
		collectingUsersDefaultHttpClient = getDefaultHttpClient();
		followingUsersGloballyDefaultHttpClient = getDefaultHttpClient();
		unfollowingUsersGloballyDefaultHttpClient = getDefaultHttpClient();
		followingUsersIndividuallyDefaultHttpClient = getDefaultHttpClient();
		unfollowingUsersIndividuallyDefaultHttpClient = getDefaultHttpClient();
		fillingUsersDefaultHttpClient = getDefaultHttpClient();
		transferingUsersDefaultHttpClient = getDefaultHttpClient();
		globallyAddingMessagesDefaultHttpClient = getDefaultHttpClient();
		individuallyAddingMessagesDefaultHttpClient = getDefaultHttpClient();
		globallyAddingCommentsDefaultHttpClient = getDefaultHttpClient();
		individuallyAddingCommentsDefaultHttpClient = getDefaultHttpClient();
	}

	public void destroy() {
		collectingBlogsDefaultHttpClient.getConnectionManager().shutdown();
		transferingBlogsDefaultHttpClient.getConnectionManager().shutdown();
		publishingBlogsDefaultHttpClient.getConnectionManager().shutdown();
		publishingMicroTasksDefaultHttpClient.getConnectionManager().shutdown();
		timinglyPublishingBlogsDefaultHttpClient.getConnectionManager()
				.shutdown();
		collectingUsersDefaultHttpClient.getConnectionManager().shutdown();
		followingUsersGloballyDefaultHttpClient.getConnectionManager()
				.shutdown();
		unfollowingUsersGloballyDefaultHttpClient.getConnectionManager()
				.shutdown();
		followingUsersIndividuallyDefaultHttpClient.getConnectionManager()
				.shutdown();
		unfollowingUsersIndividuallyDefaultHttpClient.getConnectionManager()
				.shutdown();
		fillingUsersDefaultHttpClient.getConnectionManager().shutdown();
		transferingUsersDefaultHttpClient.getConnectionManager().shutdown();
		globallyAddingMessagesDefaultHttpClient.getConnectionManager()
				.shutdown();
		individuallyAddingMessagesDefaultHttpClient.getConnectionManager()
				.shutdown();
		globallyAddingCommentsDefaultHttpClient.getConnectionManager()
				.shutdown();
		individuallyAddingCommentsDefaultHttpClient.getConnectionManager()
				.shutdown();
	}

	private DefaultHttpClient getDefaultHttpClient() {
		X509TrustManager tm = new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType)
					throws CertificateException {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType)
					throws CertificateException {
			}
		};

		SSLContext sslContext;

		try {
			sslContext = SSLContext.getInstance("TLS");

			sslContext.init(null, new TrustManager[] { tm }, null);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		} catch (KeyManagementException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		SSLSocketFactory ssf = new SSLSocketFactory(sslContext,
				SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme scheme = new Scheme("https", 443, ssf);

		SchemeRegistry registry = SchemeRegistryFactory.createDefault();
		registry.register(scheme);

		PoolingClientConnectionManager poolingClientConnectionManager = new PoolingClientConnectionManager(
				registry, 60000, TimeUnit.MILLISECONDS);
		poolingClientConnectionManager.setDefaultMaxPerRoute(10);
		poolingClientConnectionManager.setMaxTotal(100);

		BasicHttpParams basicHttpParams = new BasicHttpParams();

		basicHttpParams.setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		basicHttpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				60000);
		basicHttpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);

		return new DefaultHttpClient(poolingClientConnectionManager,
				basicHttpParams);
	}

	private void sleep() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}
	}

	private int getCurrentPpTidPageIndex(int ppTidPageSize, int ppTidPageIndex,
			int currentPpTidPageSize) {
		int currentPpTidPageIndex;

		if (ppTidPageSize == currentPpTidPageSize) {
			currentPpTidPageIndex = ppTidPageIndex;
		} else {
			currentPpTidPageIndex = ppTidPageIndex + currentPpTidPageSize
					- ppTidPageSize;

			if (currentPpTidPageIndex > currentPpTidPageSize) {
				currentPpTidPageIndex = currentPpTidPageSize;
			}

			if (currentPpTidPageIndex < 1) {
				currentPpTidPageIndex = 1;
			}
		}

		return currentPpTidPageIndex;
	}

	public void collectBlogs() {
		String ppSid;

		try {
			ppSid = ppHandler.getSid(collectingBlogsDefaultHttpClient,
					ppUsername, ppPassword);
		} catch (HandlerException e) {
			return;
		}

		sleep();

		List<PpTidType> ppTidTypeList;

		try {
			ppTidTypeList = ppTidTypeService.getPpTidTypeList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (PpTidType ppTidType : ppTidTypeList) {
			int ppTid = ppTidType.getPpTid();
			int ppTidPageSize = ppTidType.getPpTidPageSize();
			int ppTidPageIndex = ppTidType.getPpTidPageIndex();
			Type type = ppTidType.getType();
			int typeCode = type.getCode();

			int currentPpTidPageSize;

			try {
				currentPpTidPageSize = ppHandler.getTidPageSize(
						collectingBlogsDefaultHttpClient, ppSid, ppTid,
						ppTidPageSize);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			int currentPpTidPageIndex = getCurrentPpTidPageIndex(ppTidPageSize,
					ppTidPageIndex, currentPpTidPageSize);

			if (currentPpTidPageIndex > 1) {
				List<Blog> blogList = new ArrayList<Blog>();

				int blogSize = 0;

				logger.debug(
						"Begin to collect blogs, ppTid = {}, ppTidPageSize = {}, ppTidPageIndex = {}, blogSize = {}",
						ppTid, currentPpTidPageSize, currentPpTidPageIndex,
						blogSize);

				try {
					blogList = ppHandler.getBlogList(
							collectingBlogsDefaultHttpClient, ppSid, ppTid,
							currentPpTidPageIndex);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				for (Blog blog : blogList) {
					String picture = blog.getPicture();

					String pictureExt = FilenameUtils.getExtension(blog
							.getPicture());

					if (urlValidator.isValid(picture)
							&& StringUtils.isNotEmpty(pictureExt)) {
						List<Blog> descendingBlogList;

						try {
							descendingBlogList = blogService
									.getDescendingBlogList(typeCode, 0, 5);
						} catch (ServiceException e) {
							logger.error("Exception", e);

							throw new ActionException(e);
						}

						boolean existing = false;

						for (Blog descendingBlog : descendingBlogList) {
							if (descendingBlog.getText().equals(blog.getText())) {
								existing = true;

								break;
							}
						}

						if (!existing) {
							try {
								blogService.addBlog(typeCode, blog);
							} catch (ServiceException e) {
								logger.error("Exception", e);

								throw new ActionException(e);
							}

							blogSize++;
						}
					}
				}

				logger.debug(
						"End to collect blogs, ppTid = {}, ppTidPageSize = {}, ppTidPageIndex = {}, blogSize = {}",
						ppTid, currentPpTidPageSize, currentPpTidPageIndex,
						blogSize);

				currentPpTidPageIndex--;

				ppTidType.setPpTidPageSize(currentPpTidPageSize);
				ppTidType.setPpTidPageIndex(currentPpTidPageIndex);

				try {
					ppTidTypeService.updatePpTidType(ppTidType);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}
		}
	}

	private void setCookies(DefaultHttpClient defaultHttpClient, byte[] cookies) {
		List<Map<String, Object>> list;

		try {
			list = objectMapper.readValue(cookies,
					new TypeReference<List<Map<String, Object>>>() {
					});
		} catch (JsonParseException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		} catch (JsonMappingException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		} catch (IOException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		BasicCookieStore basicCookieStore = new BasicCookieStore();

		for (Map<String, Object> map : list) {
			String name = (String) map.get("name");
			String value = (String) map.get("value");

			String comment = (String) map.get("comment");
			String domain = (String) map.get("domain");

			Date expiryDate = null;
			if (map.get("expiryDate") != null) {
				expiryDate = new Date((Long) map.get("expiryDate"));
			}

			String path = (String) map.get("path");
			boolean secure = (Boolean) map.get("secure");
			int version = (Integer) map.get("version");

			BasicClientCookie basicClientCookie = new BasicClientCookie(name,
					value);

			basicClientCookie.setComment(comment);
			basicClientCookie.setDomain(domain);
			basicClientCookie.setExpiryDate(expiryDate);
			basicClientCookie.setPath(path);
			basicClientCookie.setSecure(secure);
			basicClientCookie.setVersion(version);

			basicCookieStore.addCookie(basicClientCookie);
		}

		defaultHttpClient.setCookieStore(basicCookieStore);
	}

	private byte[] getCookies(DefaultHttpClient defaultHttpClient) {
		CookieStore cookieStore = defaultHttpClient.getCookieStore();

		List<Cookie> cookieList = cookieStore.getCookies();

		try {
			return objectMapper.writeValueAsBytes(cookieList);
		} catch (JsonProcessingException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}
	}

	public void transferBlogs() {
		List<Type> typeList;

		try {
			typeList = typeService.getTypeList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (Type type : typeList) {
			int typeCode = type.getCode();

			TransferingBlogOperator transferingBlogOperator;

			try {
				transferingBlogOperator = transferingBlogOperatorService
						.getTransferingBlogOperator(typeCode);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			List<Blog> blogList;

			int transferingBlogIndex = transferingBlogOperator.getBlogIndex();

			try {
				blogList = blogService.getBlogList(typeCode,
						transferingBlogIndex, transferingBlogSize);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			if (blogList.size() < transferingBlogSize) {
				blogList.clear();
			}

			setCookies(transferingBlogsDefaultHttpClient,
					transferingBlogOperator.getCookies());

			try {
				weiboHandler.login(transferingBlogsDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			SaeStorage saeStorage;

			try {
				saeStorage = saeStorageHandler.getSaeStorage(
						transferingBlogsDefaultHttpClient, saeStorageAccessKey,
						saeStorageSecretKey);
			} catch (HandlerException e) {
				return;
			}

			sleep();

			for (Blog blog : blogList) {
				int blogIndex = transferingBlogIndex + 1;

				logger.debug(
						"Begin to transfer blog, typeCode = {}, blogIndex = {}",
						typeCode, blogIndex);

				String text = blog.getText();
				String picture = blog.getPicture();

				String prefix = "type" + typeCode + "/type" + typeCode + "_"
						+ blogIndex;

				String textFile = prefix + ".txt";
				String pictureFile = prefix + "."
						+ FilenameUtils.getExtension(picture);

				String entrance = textFile + "," + pictureFile;

				byte[] entranceBytes = entrance.getBytes(Charsets.UTF_8);
				byte[] textBytes = text.getBytes(Charsets.UTF_8);

				byte[] pictureBytes;

				HttpGet get = new HttpGet(picture);

				try {
					HttpResponse response = transferingBlogsDefaultHttpClient
							.execute(get);

					int statusCode = response.getStatusLine().getStatusCode();

					if (statusCode == HttpStatus.SC_OK) {
						pictureBytes = EntityUtils.toByteArray(response
								.getEntity());
					} else {
						break;
					}
				} catch (ClientProtocolException e) {
					break;
				} catch (IOException e) {
					break;
				} finally {
					get.releaseConnection();
				}

				String container;

				String destEntranceFile;
				String destTextFile;
				String destPictureFile;

				container = "/weibo/blogs/";

				destEntranceFile = container + prefix;
				destTextFile = container + textFile;
				destPictureFile = container + pictureFile;

				try {
					vdiskHandler.addBytes(transferingBlogsDefaultHttpClient,
							textBytes, destTextFile);
				} catch (HandlerException e) {
					break;
				}

				sleep();

				try {
					vdiskHandler.addBytes(transferingBlogsDefaultHttpClient,
							pictureBytes, destPictureFile);
				} catch (HandlerException e) {
					break;
				}

				sleep();

				try {
					vdiskHandler.addBytes(transferingBlogsDefaultHttpClient,
							entranceBytes, destEntranceFile);
				} catch (HandlerException e) {
					break;
				}

				sleep();

				container = "/blogs/";

				destEntranceFile = container + prefix;
				destTextFile = container + textFile;
				destPictureFile = container + pictureFile;

				try {
					saeStorageHandler.addBytes(
							transferingBlogsDefaultHttpClient, saeStorage,
							textBytes, destTextFile);
				} catch (HandlerException e) {
					break;
				}

				sleep();

				try {
					saeStorageHandler.addBytes(
							transferingBlogsDefaultHttpClient, saeStorage,
							pictureBytes, destPictureFile);
				} catch (HandlerException e) {
					break;
				}

				sleep();

				try {
					saeStorageHandler.addBytes(
							transferingBlogsDefaultHttpClient, saeStorage,
							entranceBytes, destEntranceFile);
				} catch (HandlerException e) {
					break;
				}

				sleep();

				logger.debug(
						"End to transfer blog, typeCode = {}, blogIndex = {}",
						typeCode, blogIndex);

				transferingBlogIndex++;
			}

			transferingBlogOperator
					.setCookies(getCookies(transferingBlogsDefaultHttpClient));
			transferingBlogOperator.setBlogIndex(transferingBlogIndex);

			try {
				transferingBlogOperatorService.updateTransferingBlogOperator(
						typeCode, transferingBlogOperator);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}
		}
	}

	public void publishBlogs() {
		List<Type> typeList;

		try {
			typeList = typeService.getTypeList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (Type type : typeList) {
			int typeCode = type.getCode();

			List<Blog> blogList;

			try {
				blogList = blogService.getRandomBlogList(typeCode, 0,
						publishingBlogSize);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			List<PublishingBlogOperator> publishingBlogOperatorList;

			try {
				publishingBlogOperatorList = publishingBlogOperatorService
						.getPublishingBlogOperatorList(typeCode);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			for (PublishingBlogOperator publishingBlogOperator : publishingBlogOperatorList) {
				setCookies(publishingBlogsDefaultHttpClient,
						publishingBlogOperator.getCookies());

				try {
					weiboHandler.login(publishingBlogsDefaultHttpClient);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				try {
					saeAppBatchhelperHandler
							.authorize(publishingBlogsDefaultHttpClient);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				for (Blog blog : blogList) {
					int blogId = blog.getId();

					logger.debug(
							"Begin to publish blog, typeCode = {}, blogId = {}",
							typeCode, blogId);

					try {
						saeAppBatchhelperHandler.publishBlog(
								publishingBlogsDefaultHttpClient, blog);
					} catch (HandlerException e) {
						continue;
					}

					logger.debug(
							"End to publish blog, typeCode = {}, blogId = {}",
							typeCode, blogId);

					sleep();
				}

				publishingBlogOperator
						.setCookies(getCookies(publishingBlogsDefaultHttpClient));

				try {
					publishingBlogOperatorService.updatePublishingBlogOperator(
							typeCode, publishingBlogOperator);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}
		}
	}

	public void timinglyPublishBlogs() {
		List<Type> typeList;

		try {
			typeList = typeService.getTypeList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (Type type : typeList) {
			int typeCode = type.getCode();

			List<TiminglyPublishingBlogOperator> timinglyPublishingBlogOperatorList;

			try {
				timinglyPublishingBlogOperatorList = timinglyPublishingBlogOperatorService
						.getTiminglyPublishingBlogOperatorList(typeCode);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			for (TiminglyPublishingBlogOperator timinglyPublishingBlogOperator : timinglyPublishingBlogOperatorList) {
				String userSn = timinglyPublishingBlogOperator.getSn();

				setCookies(timinglyPublishingBlogsDefaultHttpClient,
						timinglyPublishingBlogOperator.getCookies());

				try {
					weiboHandler
							.login(timinglyPublishingBlogsDefaultHttpClient);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				Date beginDate = timinglyPublishingBlogOperator.getBeginDate();
				Date endDate = timinglyPublishingBlogOperator.getEndDate();
				List<Integer> hourList = timinglyPublishingBlogOperator
						.getHourList();

				Date now = new Date();

				if (beginDate.before(now) && now.before(endDate)) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(now);

					int hour = cal.get(Calendar.HOUR_OF_DAY);

					if (hourList.contains(hour)) {
						try {
							saeAppBatchhelperHandler
									.authorize(timinglyPublishingBlogsDefaultHttpClient);
						} catch (HandlerException e) {
							continue;
						}

						sleep();

						List<Blog> blogList;

						int blogSize = 1;

						try {
							blogList = blogService.getRandomBlogList(typeCode,
									0, blogSize);
						} catch (ServiceException e) {
							logger.error("Exception", e);

							throw new ActionException(e);
						}

						for (Blog blog : blogList) {
							int blogId = blog.getId();

							logger.debug(
									"Begin to timingly publish blog, typeCode = {}, userSn = {}, blogId = {}",
									typeCode, userSn, blogId);

							try {
								saeAppBatchhelperHandler
										.publishBlog(
												timinglyPublishingBlogsDefaultHttpClient,
												blog);
							} catch (HandlerException e) {
								continue;
							}

							logger.debug(
									"End to timingly publish blog, typeCode = {}, userSn = {}, blogId = {}",
									typeCode, userSn, blogId);

							sleep();
						}
					}
				}

				timinglyPublishingBlogOperator
						.setCookies(getCookies(timinglyPublishingBlogsDefaultHttpClient));

				try {
					timinglyPublishingBlogOperatorService
							.updateTiminglyPublishingBlogOperator(typeCode,
									timinglyPublishingBlogOperator);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}
		}
	}

	public void publishMicroTasks() {
		List<Type> typeList;

		try {
			typeList = typeService.getTypeList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (Type type : typeList) {
			int typeCode = type.getCode();

			List<PublishingMicroTaskOperator> publishingMicroTaskOperatorList;

			try {
				publishingMicroTaskOperatorList = publishingMicroTaskOperatorService
						.getPublishingMicroTaskOperatorList(typeCode);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			for (PublishingMicroTaskOperator publishingMicroTaskOperator : publishingMicroTaskOperatorList) {
				setCookies(publishingMicroTasksDefaultHttpClient,
						publishingMicroTaskOperator.getCookies());

				try {
					microTaskHandler
							.login(publishingMicroTasksDefaultHttpClient);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				try {
					microTaskHandler
							.weiboAuth(publishingMicroTasksDefaultHttpClient);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				List<String> microTaskIdList;
				try {
					microTaskIdList = microTaskHandler
							.getMicroTaskIdList(publishingMicroTasksDefaultHttpClient);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				String microTaskId = microTaskIdList.get(RandomUtils.nextInt(0,
						microTaskIdList.size()));

				List<MicroTask> microTaskList;
				try {
					microTaskList = microTaskHandler.getMicroTaskList(
							publishingMicroTasksDefaultHttpClient, microTaskId);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				MicroTask microTask = microTaskList.get(RandomUtils.nextInt(0,
						microTaskList.size()));

				logger.debug(
						"Begin to publish micro task, typeCode = {}, id = {}",
						typeCode, microTaskId);

				try {
					microTaskHandler.publishMicroTask(
							publishingMicroTasksDefaultHttpClient, microTask);
				} catch (HandlerException e) {
					continue;
				}

				logger.debug(
						"End to publish micro task, typeCode = {}, id = {}",
						typeCode, microTaskId);

				sleep();

				publishingMicroTaskOperator
						.setCookies(getCookies(publishingMicroTasksDefaultHttpClient));

				try {
					publishingMicroTaskOperatorService
							.updatePublishingMicroTaskOperator(typeCode,
									publishingMicroTaskOperator);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}
		}
	}

	private List<User> getFollowedUserListByUserSn(HttpClient httpClient,
			String accessToken, String userSn) {
		List<User> userList;

		int cursor = 0;
		int size = 5000;

		try {
			userList = weiboApiHandler.getFollowedUserListByUserSn(httpClient,
					accessToken, userSn, cursor, size);
		} catch (HandlerException e) {
			userList = new ArrayList<User>();
		}

		sleep();

		return userList;
	}

	private void deduplicateUserList(List<User> userList) {
		Set<User> userSet = new HashSet<User>();

		userSet.addAll(userList);

		userList.clear();

		userList.addAll(userSet);
	}

	private void collectUsers() {
		QueryingUserOperator queryingUserOperator;

		try {
			queryingUserOperator = queryingUserOperatorService
					.getQueryingUserOperator();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		setCookies(collectingUsersDefaultHttpClient,
				queryingUserOperator.getCookies());

		try {
			weiboHandler.login(collectingUsersDefaultHttpClient);
		} catch (HandlerException e) {
			return;
		}

		sleep();

		String accessToken;

		try {
			accessToken = weiboApiHandler
					.getAccessToken(collectingUsersDefaultHttpClient);
		} catch (HandlerException e) {
			return;
		}

		sleep();

		List<CollectingUser> collectingUserList;

		try {
			collectingUserList = collectingUserService.getCollectingUserList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		int userSize;

		List<User> userList = new ArrayList<User>();

		userSize = userList.size();

		logger.debug("Begin to collect users, userSize = {}", userSize);

		for (CollectingUser collectingUser : collectingUserList) {
			String userSn = collectingUser.getSn();

			List<User> vUserList = getFollowedUserListByUserSn(
					collectingUsersDefaultHttpClient, accessToken, userSn);

			userList.addAll(vUserList);
		}

		deduplicateUserList(userList);

		userSize = userList.size();

		logger.debug("End to collect users, userSize = {}", userSize);

		for (User user : userList) {
			try {
				userService.addUser(UserPhase.collected, user);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}
		}

		queryingUserOperator
				.setCookies(getCookies(collectingUsersDefaultHttpClient));

		try {
			queryingUserOperatorService
					.updateQueryingUserOperator(queryingUserOperator);
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}
	}

	private void filterUsers() {
		List<User> userList;

		try {
			userList = userService.getUserList(UserPhase.collected, 0,
					filteringUserSize);
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		int userSize = userList.size();

		logger.debug("Begin to filter users, userSize = {}", userSize);

		userSize = 0;

		for (User user : userList) {
			boolean sameUserExisting = false;

			try {
				sameUserExisting = userService.isSameUserExisting(
						UserPhase.filtered, user);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			if (!sameUserExisting) {
				try {
					userService.moveUser(UserPhase.collected,
							UserPhase.filtered, user);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}

				userSize++;
			} else {
				try {
					userService.deleteUser(UserPhase.collected, user.getId());
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}
		}

		logger.debug("End to filter users, userSize = {}", userSize);
	}

	public void collectAndFilterUsers() {
		collectUsers();

		filterUsers();
	}

	public void followUsersGlobally() {
		List<FollowingUserOperator> followingUserOperatorList;

		try {
			followingUserOperatorList = followingUserOperatorService
					.getFollowingUserOperatorList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (FollowingUserOperator followingUserOperator : followingUserOperatorList) {
			int followingUserCode = followingUserOperator.getCode();
			int followingUserIndex = followingUserOperator.getUserIndex();

			List<User> userList;

			try {
				userList = userService.getUserList(UserPhase.filtered,
						followingUserIndex, 200);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			setCookies(followingUsersGloballyDefaultHttpClient,
					followingUserOperator.getCookies());

			try {
				weiboHandler.login(followingUsersGloballyDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			String accessToken;

			try {
				accessToken = weiboApiHandler
						.getAccessToken(followingUsersGloballyDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			Map<String, Integer> blogSizeMap;

			try {
				blogSizeMap = weiboApiHandler.getBlogSizeMapByUserList(
						followingUsersGloballyDefaultHttpClient, accessToken,
						userList);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			List<User> vUserList = new ArrayList<User>();

			for (int i = 0; i < userList.size(); i++) {
				User user = userList.get(i);

				String userSn = user.getSn();

				int blogSize = blogSizeMap.get(userSn);

				if (blogSize >= 5) {
					vUserList.add(user);
				}

				followingUserIndex++;

				if (vUserList.size() == followingUserSize) {
					break;
				}
			}

			int userSize = vUserList.size();

			logger.debug(
					"Begin to follow users globally, followingUserCode = {}, userSize = {}",
					followingUserCode, userSize);

			userSize = 0;

			for (User user : vUserList) {
				String userSn = user.getSn();

				try {
					weiboHandler.follow(
							followingUsersGloballyDefaultHttpClient, userSn);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				userSize++;

				try {
					followedUserService.addUser(followingUserCode, user);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}

			logger.debug(
					"End to follow users globally, followingUserCode = {}, userSize = {}",
					followingUserCode, userSize);

			followingUserOperator
					.setCookies(getCookies(followingUsersGloballyDefaultHttpClient));
			followingUserOperator.setUserIndex(followingUserIndex);

			try {
				followingUserOperatorService
						.updateFollowingUserOperator(followingUserOperator);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}
		}
	}

	public void unfollowUsersGlobally() {
		List<FollowingUserOperator> followingUserOperatorList;

		try {
			followingUserOperatorList = followingUserOperatorService
					.getFollowingUserOperatorList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (FollowingUserOperator followingUserOperator : followingUserOperatorList) {
			int followingUserCode = followingUserOperator.getCode();

			List<User> userList;

			try {
				userList = followedUserService.getUserListBeforeDays(
						followingUserCode, reservingFollowedDays, 0,
						followingUserSize * 2);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			setCookies(unfollowingUsersGloballyDefaultHttpClient,
					followingUserOperator.getCookies());

			try {
				weiboHandler.login(unfollowingUsersGloballyDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			int userSize = userList.size();

			logger.debug(
					"Begin to unfollow users globally, followingUserCode = {}, userSize = {}",
					followingUserCode, userSize);

			userSize = 0;

			for (User user : userList) {
				String userSn = user.getSn();

				try {
					weiboHandler.unfollow(
							unfollowingUsersGloballyDefaultHttpClient, userSn);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				userSize++;

				try {
					followedUserService.deleteUser(followingUserCode,
							user.getId());
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}

			logger.debug(
					"End to unfollow users globally, followingUserCode = {}, userSize = {}",
					followingUserCode, userSize);

			try {
				userList = followedUserService.getUserListBeforeDays(
						followingUserCode, reservingFollowedDays + 1, 0,
						followingUserSize * 2);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			for (User user : userList) {
				try {
					followedUserService.deleteUser(followingUserCode,
							user.getId());
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}

			followingUserOperator
					.setCookies(getCookies(unfollowingUsersGloballyDefaultHttpClient));

			try {
				followingUserOperatorService
						.updateFollowingUserOperator(followingUserOperator);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}
		}
	}

	public void followUsersIndividually() {
		List<Type> typeList;

		try {
			typeList = typeService.getTypeList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (Type type : typeList) {
			int typeCode = type.getCode();

			List<FollowingUserOperator> followingUserOperatorList;

			try {
				followingUserOperatorList = followingUserOperatorService
						.getFollowingUserOperatorList(typeCode);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			for (FollowingUserOperator followingUserOperator : followingUserOperatorList) {
				int followingUserCode = followingUserOperator.getCode();

				List<User> userList;

				try {
					userList = userService.getRandomUserList(
							UserPhase.filtered, 0, 100);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}

				setCookies(followingUsersIndividuallyDefaultHttpClient,
						followingUserOperator.getCookies());

				try {
					weiboHandler
							.login(followingUsersIndividuallyDefaultHttpClient);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				String accessToken;

				try {
					accessToken = weiboApiHandler
							.getAccessToken(followingUsersIndividuallyDefaultHttpClient);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				Map<String, Integer> blogSizeMap;

				try {
					blogSizeMap = weiboApiHandler.getBlogSizeMapByUserList(
							followingUsersIndividuallyDefaultHttpClient,
							accessToken, userList);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				List<User> vUserList = new ArrayList<User>();

				for (int i = 0; i < userList.size(); i++) {
					User user = userList.get(i);

					String userSn = user.getSn();

					int blogSize = blogSizeMap.get(userSn);

					if (blogSize >= 5) {
						vUserList.add(user);
					}

					if (vUserList.size() == followingUserSize) {
						break;
					}
				}

				int userSize = vUserList.size();

				logger.debug(
						"Begin to follow users individually, typeCode = {}, followingUserCode = {}, userSize = {}",
						typeCode, followingUserCode, userSize);

				userSize = 0;

				for (User user : vUserList) {
					String userSn = user.getSn();

					try {
						weiboHandler.follow(
								followingUsersIndividuallyDefaultHttpClient,
								userSn);
					} catch (HandlerException e) {

					}

					// sleep();

					userSize++;

					try {
						followedUserService.addUser(typeCode,
								followingUserCode, user);
					} catch (ServiceException e) {
						logger.error("Exception", e);

						throw new ActionException(e);
					}
				}

				logger.debug(
						"End to follow users individually, typeCode = {}, followingUserCode = {}, userSize = {}",
						typeCode, followingUserCode, userSize);

				followingUserOperator
						.setCookies(getCookies(followingUsersIndividuallyDefaultHttpClient));

				try {
					followingUserOperatorService.updateFollowingUserOperator(
							typeCode, followingUserOperator);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}
		}
	}

	public void unfollowUsersIndividually() {
		List<Type> typeList;

		try {
			typeList = typeService.getTypeList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (Type type : typeList) {
			int typeCode = type.getCode();

			List<FollowingUserOperator> followingUserOperatorList;

			try {
				followingUserOperatorList = followingUserOperatorService
						.getFollowingUserOperatorList(typeCode);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			for (FollowingUserOperator followingUserOperator : followingUserOperatorList) {
				int followingUserCode = followingUserOperator.getCode();

				List<User> userList;

				try {
					userList = followedUserService.getUserListBeforeDays(
							typeCode, followingUserCode, reservingFollowedDays,
							0, followingUserSize * 2);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}

				setCookies(unfollowingUsersIndividuallyDefaultHttpClient,
						followingUserOperator.getCookies());

				try {
					weiboHandler
							.login(unfollowingUsersIndividuallyDefaultHttpClient);
				} catch (HandlerException e) {
					continue;
				}

				sleep();

				int userSize = userList.size();

				logger.debug(
						"Begin to unfollow users individually, typeCode = {}, followingUserCode = {}, userSize = {}",
						typeCode, followingUserCode, userSize);

				userSize = 0;

				for (User user : userList) {
					String userSn = user.getSn();

					try {
						weiboHandler.unfollow(
								unfollowingUsersIndividuallyDefaultHttpClient,
								userSn);
					} catch (HandlerException e) {
						continue;
					}

					// sleep();

					userSize++;

					try {
						followedUserService.deleteUser(typeCode,
								followingUserCode, user.getId());
					} catch (ServiceException e) {
						logger.error("Exception", e);

						throw new ActionException(e);
					}
				}

				logger.debug(
						"End to unfollow users individually, typeCode = {}, followingUserCode = {}, userSize = {}",
						typeCode, followingUserCode, userSize);

				try {
					userList = followedUserService
							.getUserListBeforeDays(typeCode, followingUserCode,
									reservingFollowedDays + 1, 0,
									followingUserSize * 2);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}

				for (User user : userList) {
					try {
						followedUserService.deleteUser(typeCode,
								followingUserCode, user.getId());
					} catch (ServiceException e) {
						logger.error("Exception", e);

						throw new ActionException(e);
					}
				}

				followingUserOperator
						.setCookies(getCookies(unfollowingUsersIndividuallyDefaultHttpClient));

				try {
					followingUserOperatorService.updateFollowingUserOperator(
							typeCode, followingUserOperator);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}
		}
	}

	public void fillUsers() {
		FillingUserOperator fillingUserOperator;

		try {
			fillingUserOperator = fillingUserOperatorService
					.getFillingUserOperator();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		setCookies(fillingUsersDefaultHttpClient,
				fillingUserOperator.getCookies());

		try {
			weiboHandler.login(fillingUsersDefaultHttpClient);
		} catch (HandlerException e) {
			return;
		}

		sleep();

		String accessToken;

		try {
			accessToken = weiboApiHandler
					.getAccessToken(fillingUsersDefaultHttpClient);
		} catch (HandlerException e) {
			return;
		}

		sleep();

		int fillingUserIndex = fillingUserOperator.getUserIndex();

		List<User> userList;

		try {
			userList = userService.getUserList(UserPhase.filtered,
					fillingUserIndex, fillingUserSize);
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		if (userList.size() < fillingUserSize) {
			userList.clear();
		}

		int userSize = userList.size();

		fillingUserIndex = fillingUserIndex + userSize;

		logger.debug("Begin to fill users, userSize = {}", userSize);

		userSize = 0;

		for (User user : userList) {
			String userSn = user.getSn();

			UserProfile userProfile = new UserProfile();

			try {
				weiboApiHandler.fillUserProfileByUserSn(
						fillingUsersDefaultHttpClient, accessToken, userSn,
						userProfile);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			try {
				weiboApiHandler.fillUserProfileTagListByUserSn(
						fillingUsersDefaultHttpClient, accessToken, userSn,
						userProfile);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			List<UserProfile> descendingUserProfileList;

			try {
				descendingUserProfileList = userProfileService
						.getDescendingUserProfileList(0, fillingUserSize);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			boolean existing = false;

			for (UserProfile descendingUserProfile : descendingUserProfileList) {
				if (descendingUserProfile.getSn().equals(userSn)) {
					existing = true;

					break;
				}
			}

			if (!existing) {
				try {
					userProfileService.addUserProfile(userProfile);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}

				userSize++;
			}
		}

		logger.debug("End to fill users, userSize = {}", userSize);

		fillingUserOperator
				.setCookies(getCookies(fillingUsersDefaultHttpClient));
		fillingUserOperator.setUserIndex(fillingUserIndex);

		try {
			fillingUserOperatorService
					.updateFillingUserOperator(fillingUserOperator);
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}
	}

	public void transferUsers() {
		TransferingUserOperator transferingUserOperator;

		try {
			transferingUserOperator = transferingUserOperatorService
					.getTransferingUserOperator();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		int transferingUserIndex = transferingUserOperator.getUserIndex();

		List<UserProfile> userProfileList;

		try {
			userProfileList = userProfileService.getUserProfileList(
					transferingUserIndex, transferingUserSize);
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		if (userProfileList.size() < transferingUserSize) {
			userProfileList.clear();
		}

		setCookies(transferingUsersDefaultHttpClient,
				transferingUserOperator.getCookies());

		try {
			weiboHandler.login(transferingUsersDefaultHttpClient);
		} catch (HandlerException e) {
			return;
		}

		sleep();

		if (!userProfileList.isEmpty()) {
			List<Map<String, Object>> userProfileMapList = new ArrayList<Map<String, Object>>();

			for (UserProfile userProfile : userProfileList) {
				Map<String, Object> userProfileMap = new LinkedHashMap<String, Object>();

				userProfileMap.put("sn", userProfile.getSn());
				userProfileMap.put("name", userProfile.getName());

				String gender = userProfile.getGender();

				if (StringUtils.isNotEmpty(gender)) {
					userProfileMap.put("gender", gender);
				}

				String location = userProfile.getLocation();

				if (StringUtils.isNotEmpty(location)) {
					userProfileMap.put("location", location);
				}

				List<String> tagList = userProfile.getTagList();

				if (tagList != null && !tagList.isEmpty()) {
					userProfileMap.put("tagList", tagList);
				}

				userProfileMapList.add(userProfileMap);
			}

			int beginUserIndex = transferingUserIndex + 1;
			int endUserIndex = transferingUserIndex + userProfileMapList.size();

			logger.debug(
					"Begin to transfer users, beginUserIndex = {}, endUserIndex = {}",
					beginUserIndex, endUserIndex);

			byte[] userProfilesBytes;

			try {
				userProfilesBytes = objectMapper
						.writeValueAsBytes(userProfileMapList);
			} catch (JsonProcessingException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			String destFileName = beginUserIndex + "-" + endUserIndex;

			String destFile;

			destFile = "/weibo/users/" + destFileName + ".json";

			try {
				vdiskHandler.addBytes(transferingUsersDefaultHttpClient,
						userProfilesBytes, destFile);
			} catch (HandlerException e) {
				return;
			}

			sleep();
			
			/*
			SaeStorage saeStorage;

			try {
				saeStorage = saeStorageHandler.getSaeStorage(
						transferingUsersDefaultHttpClient, saeStorageAccessKey,
						saeStorageSecretKey);
			} catch (HandlerException e) {
				return;
			}

			sleep();

			destFile = "/users/" + destFileName + ".json";

			try {
				saeStorageHandler.addBytes(transferingUsersDefaultHttpClient,
						saeStorage, userProfilesBytes, destFile);
			} catch (HandlerException e) {
				return;
			}

			sleep();
			*/

			logger.debug(
					"End to transfer users, beginUserIndex = {}, endUserIndex = {}",
					beginUserIndex, endUserIndex);
		}

		transferingUserIndex = transferingUserIndex + userProfileList.size();

		transferingUserOperator
				.setCookies(getCookies(transferingUsersDefaultHttpClient));
		transferingUserOperator.setUserIndex(transferingUserIndex);

		try {
			transferingUserOperatorService
					.updateTransferingUserOperator(transferingUserOperator);
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}
	}

	public void globallyAddMessages() {
		List<GloballyAddingMessageOperator> globallyAddingMessageOperatorList;

		try {
			globallyAddingMessageOperatorList = globallyAddingMessageOperatorService
					.getGloballyAddingMessageOperatorList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (GloballyAddingMessageOperator globallyAddingMessageOperator : globallyAddingMessageOperatorList) {
			String userSn = globallyAddingMessageOperator.getSn();

			setCookies(globallyAddingMessagesDefaultHttpClient,
					globallyAddingMessageOperator.getCookies());

			try {
				weiboHandler.login(globallyAddingMessagesDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			String text = globallyAddingMessageOperator.getText();

			int userSize = globallyAddingMessageOperator.getUserSize();

			Date beginDate = globallyAddingMessageOperator.getBeginDate();
			Date endDate = globallyAddingMessageOperator.getEndDate();
			List<Integer> hourList = globallyAddingMessageOperator
					.getHourList();

			Date now = new Date();

			if (beginDate.before(now) && now.before(endDate)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(now);

				int hour = cal.get(Calendar.HOUR_OF_DAY);

				if (hourList.contains(hour)) {
					List<UserProfile> userProfileList;

					try {
						userProfileList = userProfileService
								.getRandomUserProfileList(0, userSize);
					} catch (ServiceException e) {
						logger.error("Exception", e);

						throw new ActionException(e);
					}

					for (UserProfile userProfile : userProfileList) {
						String userName = userProfile.getName();

						logger.debug(
								"Begin to globally add message, hostUserSn = {}, guestUserSn = {}",
								userSn, userProfile.getSn());

						try {
							weiboHandler.addMessage(
									globallyAddingMessagesDefaultHttpClient,
									userName, "你好，" + userName + "。" + text);
						} catch (HandlerException e) {
							continue;
						}

						logger.debug(
								"End to globally add message, hostUserSn = {}, guestUserSn = {}",
								userSn, userProfile.getSn());

						sleep();
					}
				}
			}

			globallyAddingMessageOperator
					.setCookies(getCookies(globallyAddingMessagesDefaultHttpClient));

			try {
				globallyAddingMessageOperatorService
						.updateGloballyAddingMessageOperator(globallyAddingMessageOperator);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}
		}
	}

	public void individuallyAddMessages() {
		List<IndividuallyAddingMessageOperator> individuallyAddingMessageOperatorList;

		try {
			individuallyAddingMessageOperatorList = individuallyAddingMessageOperatorService
					.getIndividuallyAddingMessageOperatorList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (IndividuallyAddingMessageOperator individuallyAddingMessageOperator : individuallyAddingMessageOperatorList) {
			String userSn = individuallyAddingMessageOperator.getSn();

			setCookies(individuallyAddingMessagesDefaultHttpClient,
					individuallyAddingMessageOperator.getCookies());

			try {
				weiboHandler.login(individuallyAddingMessagesDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			String text = individuallyAddingMessageOperator.getText();

			int userSize = individuallyAddingMessageOperator.getUserSize();

			Date beginDate = individuallyAddingMessageOperator.getBeginDate();
			Date endDate = individuallyAddingMessageOperator.getEndDate();
			List<Integer> hourList = individuallyAddingMessageOperator
					.getHourList();

			Date now = new Date();

			if (beginDate.before(now) && now.before(endDate)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(now);

				int hour = cal.get(Calendar.HOUR_OF_DAY);

				if (hourList.contains(hour)) {
					List<UserBase> userBaseList;

					String userBaseTableName = individuallyAddingMessageOperator
							.getUserBaseTableName();
					int userBaseIndex = individuallyAddingMessageOperator
							.getUserBaseIndex();

					try {
						userBaseList = userBaseService.getUserBaseList(
								userBaseTableName, userBaseIndex, userSize);
					} catch (ServiceException e) {
						logger.error("Exception", e);

						throw new ActionException(e);
					}

					for (UserBase userBase : userBaseList) {
						String userName = userBase.getName();

						logger.debug(
								"Begin to individually add message, hostUserSn = {}, guestUserSn = {}",
								userSn, userBase.getSn());

						boolean successful = false;

						for (int i = 0; i < 10; i++) {
							try {
								weiboHandler
										.addMessage(
												individuallyAddingMessagesDefaultHttpClient,
												userName, "你好，" + userName
														+ "。" + text);

								successful = true;
								break;
							} catch (HandlerException e) {
							}

							sleep();
						}

						if (!successful) {
							continue;
						}

						logger.debug(
								"End to individually add message, hostUserSn = {}, guestUserSn = {}",
								userSn, userBase.getSn());

						sleep();
					}

					individuallyAddingMessageOperator
							.setUserBaseIndex(userBaseIndex + userSize);
				}
			}

			individuallyAddingMessageOperator
					.setCookies(getCookies(individuallyAddingMessagesDefaultHttpClient));

			try {
				individuallyAddingMessageOperatorService
						.updateIndividuallyAddingMessageOperator(individuallyAddingMessageOperator);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}
		}
	}

	public void globallyAddComments() {
		List<GloballyAddingCommentOperator> globallyAddingCommentOperatorList;

		try {
			globallyAddingCommentOperatorList = globallyAddingCommentOperatorService
					.getGloballyAddingCommentOperatorList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (GloballyAddingCommentOperator globallyAddingCommentOperator : globallyAddingCommentOperatorList) {
			String userSn = globallyAddingCommentOperator.getSn();

			setCookies(globallyAddingCommentsDefaultHttpClient,
					globallyAddingCommentOperator.getCookies());

			try {
				weiboHandler.login(globallyAddingCommentsDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			String text = globallyAddingCommentOperator.getText();

			int userSize = globallyAddingCommentOperator.getUserSize();

			Date beginDate = globallyAddingCommentOperator.getBeginDate();
			Date endDate = globallyAddingCommentOperator.getEndDate();
			List<Integer> hourList = globallyAddingCommentOperator
					.getHourList();

			Date now = new Date();

			if (beginDate.before(now) && now.before(endDate)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(now);

				int hour = cal.get(Calendar.HOUR_OF_DAY);

				if (hourList.contains(hour)) {
					List<UserProfile> userProfileList;

					try {
						userProfileList = userProfileService
								.getRandomUserProfileList(0, userSize);
					} catch (ServiceException e) {
						logger.error("Exception", e);

						throw new ActionException(e);
					}

					for (UserProfile userProfile : userProfileList) {
						String userName = userProfile.getName();

						logger.debug(
								"Begin to globally add comment, hostUserSn = {}, guestUserSn = {}",
								userSn, userProfile.getSn());

						List<String> blogSnList = new ArrayList<String>();

						try {
							blogSnList = weiboHandler.getBlogSnList(
									globallyAddingCommentsDefaultHttpClient,
									userProfile.getSn());
						} catch (HandlerException e) {
							continue;
						}

						sleep();

						if (!blogSnList.isEmpty()) {
							try {
								weiboHandler
										.addComment(
												globallyAddingCommentsDefaultHttpClient,
												blogSnList.get(0), "你好，"
														+ userName + "。" + text);
							} catch (HandlerException e) {
								continue;
							}
						}

						logger.debug(
								"End to globally add comment, hostUserSn = {}, guestUserSn = {}",
								userSn, userProfile.getSn());

						sleep();
					}
				}
			}

			globallyAddingCommentOperator
					.setCookies(getCookies(globallyAddingCommentsDefaultHttpClient));

			try {
				globallyAddingCommentOperatorService
						.updateGloballyAddingCommentOperator(globallyAddingCommentOperator);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}
		}
	}

	public void individuallyAddComments() {
		List<IndividuallyAddingCommentOperator> individuallyAddingCommentOperatorList;

		try {
			individuallyAddingCommentOperatorList = individuallyAddingCommentOperatorService
					.getIndividuallyAddingCommentOperatorList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (IndividuallyAddingCommentOperator individuallyAddingCommentOperator : individuallyAddingCommentOperatorList) {
			String userSn = individuallyAddingCommentOperator.getSn();

			setCookies(individuallyAddingCommentsDefaultHttpClient,
					individuallyAddingCommentOperator.getCookies());

			try {
				weiboHandler.login(individuallyAddingCommentsDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			String text = individuallyAddingCommentOperator.getText();

			int userSize = individuallyAddingCommentOperator.getUserSize();

			Date beginDate = individuallyAddingCommentOperator.getBeginDate();
			Date endDate = individuallyAddingCommentOperator.getEndDate();
			List<Integer> hourList = individuallyAddingCommentOperator
					.getHourList();

			Date now = new Date();

			if (beginDate.before(now) && now.before(endDate)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(now);

				int hour = cal.get(Calendar.HOUR_OF_DAY);

				if (hourList.contains(hour)) {
					List<UserBase> userBaseList;

					String userBaseTableName = individuallyAddingCommentOperator
							.getUserBaseTableName();
					int userBaseIndex = individuallyAddingCommentOperator
							.getUserBaseIndex();

					try {
						userBaseList = userBaseService.getUserBaseList(
								userBaseTableName, userBaseIndex, userSize);
					} catch (ServiceException e) {
						logger.error("Exception", e);

						throw new ActionException(e);
					}

					for (UserBase userBase : userBaseList) {
						String userName = userBase.getName();

						logger.debug(
								"Begin to individually add comment, hostUserSn = {}, guestUserSn = {}",
								userSn, userBase.getSn());

						boolean successful = false;

						List<String> blogSnList = new ArrayList<String>();

						for (int i = 0; i < 10; i++) {
							try {
								blogSnList = weiboHandler
										.getBlogSnList(
												individuallyAddingCommentsDefaultHttpClient,
												userBase.getSn());

								successful = true;
								break;
							} catch (HandlerException e) {
							}
						}

						if (!successful) {
							continue;
						}

						sleep();

						if (blogSnList.isEmpty()) {
							continue;
						}

						for (int i = 0; i < 10; i++) {
							try {
								weiboHandler
										.addComment(
												individuallyAddingCommentsDefaultHttpClient,
												blogSnList.get(0), "你好，"
														+ userName + "。" + text);

								successful = true;
								break;
							} catch (HandlerException e) {
							}
						}

						if (!successful) {
							continue;
						}

						logger.debug(
								"End to individually add comment, hostUserSn = {}, guestUserSn = {}",
								userSn, userBase.getSn());

						sleep();
					}

					individuallyAddingCommentOperator
							.setUserBaseIndex(userBaseIndex + userSize);
				}
			}

			individuallyAddingCommentOperator
					.setCookies(getCookies(individuallyAddingCommentsDefaultHttpClient));

			try {
				individuallyAddingCommentOperatorService
						.updateIndividuallyAddingCommentOperator(individuallyAddingCommentOperator);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}
		}
	}

}
