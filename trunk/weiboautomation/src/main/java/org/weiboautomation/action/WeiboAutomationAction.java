package org.weiboautomation.action;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FilenameUtils;
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
import org.weiboautomation.entity.FollowingUser;
import org.weiboautomation.entity.PpTidType;
import org.weiboautomation.entity.PublishingUser;
import org.weiboautomation.entity.QueryingUser;
import org.weiboautomation.entity.SaeStorage;
import org.weiboautomation.entity.TransferingUser;
import org.weiboautomation.entity.Type;
import org.weiboautomation.entity.User;
import org.weiboautomation.entity.UserPhase;
import org.weiboautomation.handler.PpHandler;
import org.weiboautomation.handler.SaeAppBatchhelperHandler;
import org.weiboautomation.handler.SaeStorageHandler;
import org.weiboautomation.handler.VdiskHandler;
import org.weiboautomation.handler.WeiboApiHandler;
import org.weiboautomation.handler.WeiboHandler;
import org.weiboautomation.handler.exception.HandlerException;
import org.weiboautomation.service.BlogService;
import org.weiboautomation.service.CollectingUserService;
import org.weiboautomation.service.FollowedUserService;
import org.weiboautomation.service.FollowingUserService;
import org.weiboautomation.service.PpTidTypeService;
import org.weiboautomation.service.PublishingUserService;
import org.weiboautomation.service.QueryingUserService;
import org.weiboautomation.service.TransferingUserService;
import org.weiboautomation.service.TypeService;
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

	private TransferingUserService transferingUserService;

	private int transferingBlogSize;

	private WeiboHandler weiboHandler;

	private TypeService typeService;

	private VdiskHandler vdiskHandler;

	private SaeStorageHandler saeStorageHandler;

	private String saeStorageAccessKey;

	private String saeStorageSecretKey;

	private int publishingBlogSize;

	private PublishingUserService publishingUserService;

	private SaeAppBatchhelperHandler saeAppBatchhelperHandler;

	private QueryingUserService queryingUserService;

	private CollectingUserService collectingUserService;

	private WeiboApiHandler weiboApiHandler;

	private UserService userService;

	private int filteringUserSize;

	private FollowingUserService followingUserService;

	private int followingUserSize;

	private FollowedUserService followedUserService;

	private int reservingFollowedDays;

	private ObjectMapper objectMapper;

	private UrlValidator urlValidator;

	private DefaultHttpClient collectingBlogsDefaultHttpClient;

	private DefaultHttpClient transferingBlogsDefaultHttpClient;

	private DefaultHttpClient publishingBlogsDefaultHttpClient;

	private DefaultHttpClient collectingUsersDefaultHttpClient;

	private DefaultHttpClient followingUsersDefaultHttpClient;

	private DefaultHttpClient unfollowingUsersDefaultHttpClient;

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

	public void setTransferingUserService(
			TransferingUserService transferingUserService) {
		this.transferingUserService = transferingUserService;
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

	public void setPublishingUserService(
			PublishingUserService publishingUserService) {
		this.publishingUserService = publishingUserService;
	}

	public void setSaeAppBatchhelperHandler(
			SaeAppBatchhelperHandler saeAppBatchhelperHandler) {
		this.saeAppBatchhelperHandler = saeAppBatchhelperHandler;
	}

	public void setQueryingUserService(QueryingUserService queryingUserService) {
		this.queryingUserService = queryingUserService;
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

	public void setFollowingUserService(
			FollowingUserService followingUserService) {
		this.followingUserService = followingUserService;
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

	public void initialize() {
		objectMapper = new ObjectMapper();

		urlValidator = UrlValidator.getInstance();

		collectingBlogsDefaultHttpClient = getDefaultHttpClient();
		transferingBlogsDefaultHttpClient = getDefaultHttpClient();
		publishingBlogsDefaultHttpClient = getDefaultHttpClient();
		collectingUsersDefaultHttpClient = getDefaultHttpClient();
		followingUsersDefaultHttpClient = getDefaultHttpClient();
		unfollowingUsersDefaultHttpClient = getDefaultHttpClient();
	}

	public void destroy() {
		collectingBlogsDefaultHttpClient.getConnectionManager().shutdown();
		transferingBlogsDefaultHttpClient.getConnectionManager().shutdown();
		publishingBlogsDefaultHttpClient.getConnectionManager().shutdown();
		collectingUsersDefaultHttpClient.getConnectionManager().shutdown();
		followingUsersDefaultHttpClient.getConnectionManager().shutdown();
		unfollowingUsersDefaultHttpClient.getConnectionManager().shutdown();
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

		SSLSocketFactory ssf = new SSLSocketFactory(sslContext);
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
						try {
							blogService.addBlog(typeCode, blog);
						} catch (ServiceException e) {
							logger.error("Exception", e);

							throw new ActionException(e);
						}

						blogSize++;
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
				expiryDate = new Date((long) map.get("expiryDate"));
			}

			String path = (String) map.get("path");
			boolean secure = (boolean) map.get("secure");
			int version = (int) map.get("version");

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
		byte[] cookies;

		CookieStore cookieStore = defaultHttpClient.getCookieStore();

		List<Cookie> cookieList = cookieStore.getCookies();

		try {
			cookies = objectMapper.writeValueAsBytes(cookieList);
		} catch (JsonProcessingException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		return cookies;
	}

	public void transferBlogs() {
		SaeStorage saeStorage;

		try {
			saeStorage = saeStorageHandler.getSaeStorage(
					transferingBlogsDefaultHttpClient, saeStorageAccessKey,
					saeStorageSecretKey);
		} catch (HandlerException e) {
			return;
		}

		sleep();

		List<Type> typeList;

		try {
			typeList = typeService.getTypeList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (Type type : typeList) {
			int typeCode = type.getCode();

			TransferingUser transferingUser;

			try {
				transferingUser = transferingUserService
						.getTransferingUser(typeCode);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			List<Blog> blogList;

			int transferingBlogIndex = transferingUser.getBlogIndex();

			try {
				blogList = blogService.getBlogList(typeCode,
						transferingBlogIndex, transferingBlogSize);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			setCookies(transferingBlogsDefaultHttpClient,
					transferingUser.getCookies());

			try {
				weiboHandler.refresh(transferingBlogsDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			for (Blog blog : blogList) {
				int blogIndex = transferingBlogIndex + 1;

				logger.debug(
						"Begin to transfer blog, typeCode = {}, blogIndex = {}",
						typeCode, blogIndex);

				String text = blog.getText();
				String picture = blog.getPicture();

				String prefix = "/type" + typeCode + "/type" + typeCode + "_"
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

				container = "/weiboautomation/blogs";

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

				container = "/contentlib";

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

			transferingUser
					.setCookies(getCookies(transferingBlogsDefaultHttpClient));

			transferingUser.setBlogIndex(transferingBlogIndex);

			try {
				transferingUserService.updateTransferingUser(typeCode,
						transferingUser);
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

			List<PublishingUser> publishingUserList;

			try {
				publishingUserList = publishingUserService
						.getPublishingUserList(typeCode);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			for (PublishingUser publishingUser : publishingUserList) {
				setCookies(publishingBlogsDefaultHttpClient,
						publishingUser.getCookies());

				try {
					weiboHandler.refresh(publishingBlogsDefaultHttpClient);
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

				publishingUser
						.setCookies(getCookies(publishingBlogsDefaultHttpClient));

				try {
					publishingUserService.updatePublishingUser(typeCode,
							publishingUser);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}
		}
	}

	private List<User> getFollowedUserListByUserSn(HttpClient httpClient,
			String accessToken, String userSn) {
		List<User> userList = new ArrayList<User>();

		int cursor = 0;
		int size = 100;

		for (int i = 0; i < 50; i++) {
			List<User> vUserList;

			try {
				vUserList = weiboApiHandler.getFollowedUserListByUserSn(
						httpClient, accessToken, userSn, cursor, size);
			} catch (HandlerException e) {
				vUserList = new ArrayList<User>();
			}

			sleep();

			userList.addAll(vUserList);

			cursor = cursor + size;
		}

		return userList;
	}

	private void deduplicateUserList(List<User> userList) {
		Set<User> userSet = new HashSet<User>();

		userSet.addAll(userList);

		userList.clear();

		userList.addAll(userSet);
	}

	private void collectUsers() {
		QueryingUser queryingUser;

		try {
			queryingUser = queryingUserService.getQueryingUser();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		setCookies(collectingUsersDefaultHttpClient, queryingUser.getCookies());

		try {
			weiboHandler.refresh(collectingUsersDefaultHttpClient);
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

		queryingUser.setCookies(getCookies(collectingUsersDefaultHttpClient));

		try {
			queryingUserService.updateQueryingUser(queryingUser);
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

	private void followUsers() {
		List<FollowingUser> followingUserList;

		try {
			followingUserList = followingUserService.getFollowingUserList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (FollowingUser followingUser : followingUserList) {
			int followingUserCode = followingUser.getCode();
			int followingUserIndex = followingUser.getUserIndex();

			List<User> userList;

			try {
				userList = userService.getUserList(UserPhase.filtered,
						followingUserIndex, 200);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			setCookies(followingUsersDefaultHttpClient,
					followingUser.getCookies());

			try {
				weiboHandler.refresh(followingUsersDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			String accessToken;

			try {
				accessToken = weiboApiHandler
						.getAccessToken(followingUsersDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			Map<String, Integer> blogSizeMap;

			try {
				blogSizeMap = weiboApiHandler.getBlogSizeMapByUserList(
						followingUsersDefaultHttpClient, accessToken, userList);
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
					"Begin to follow users, followingUserCode = {}, userSize = {}",
					followingUserCode, userSize);

			userSize = 0;

			for (User user : vUserList) {
				String userSn = user.getSn();

				try {
					weiboHandler
							.follow(followingUsersDefaultHttpClient, userSn);

					userSize++;
				} catch (HandlerException e) {

				}

				sleep();

				try {
					followedUserService.addUser(followingUserCode, user);
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}

			logger.debug(
					"End to follow users, followingUserCode = {}, userSize = {}",
					followingUserCode, userSize);

			followingUser.setUserIndex(followingUserIndex);

			followingUser
					.setCookies(getCookies(followingUsersDefaultHttpClient));

			try {
				followingUserService.updateFollowingUser(followingUser);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}
		}
	}

	private void unfollowUsers() {
		List<FollowingUser> followingUserList;

		try {
			followingUserList = followingUserService.getFollowingUserList();
		} catch (ServiceException e) {
			logger.error("Exception", e);

			throw new ActionException(e);
		}

		for (FollowingUser followingUser : followingUserList) {
			int followingUserCode = followingUser.getCode();

			List<User> userList;

			try {
				userList = followedUserService.getUserListBeforeDays(
						followingUserCode, reservingFollowedDays);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}

			setCookies(unfollowingUsersDefaultHttpClient,
					followingUser.getCookies());

			try {
				weiboHandler.refresh(unfollowingUsersDefaultHttpClient);
			} catch (HandlerException e) {
				continue;
			}

			sleep();

			int userSize = userList.size();

			logger.debug(
					"Begin to unfollow users, followingUserCode = {}, userSize = {}",
					followingUserCode, userSize);

			userSize = 0;

			for (User user : userList) {
				String userSn = user.getSn();

				try {
					weiboHandler.unfollow(unfollowingUsersDefaultHttpClient,
							userSn);

					userSize++;
				} catch (HandlerException e) {

				}

				sleep();

				try {
					followedUserService.deleteUser(followingUserCode,
							user.getId());
				} catch (ServiceException e) {
					logger.error("Exception", e);

					throw new ActionException(e);
				}
			}

			logger.debug(
					"End to unfollow users, followingUserCode = {}, userSize = {}",
					followingUserCode, userSize);

			followingUser
					.setCookies(getCookies(unfollowingUsersDefaultHttpClient));

			try {
				followingUserService.updateFollowingUser(followingUser);
			} catch (ServiceException e) {
				logger.error("Exception", e);

				throw new ActionException(e);
			}
		}
	}

	public void followAndUnfollowUsers() {
		followUsers();

		unfollowUsers();
	}

}
