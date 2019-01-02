package com.saintdan.framework.po;

import com.google.common.collect.Sets;
import com.saintdan.framework.constant.CommonsConstant;
import com.saintdan.framework.tools.UUIDGenId;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * Authorized client, provide for spring security.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 10/23/15
 * @since JDK1.8
 */
@Table(name = "clients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client implements ClientDetails {

  private static final long serialVersionUID = 6500601540965188191L;

  @Id
  @KeySql(genId = UUIDGenId.class)
  @Column(name = "id", updatable = false)
  private String id;

  @Column(name = "client_id_alias")
  private String clientIdAlias;

  @Column(name = "resource_id_str")
  private String resourceIdStr;

  @Column(name = "client_secret_alias")
  private String clientSecretAlias;

  /**
   * Available values: read, write
   */
  @Column(name = "scope_str")
  private String scopeStr;

  /**
   * grant types include "authorization_code", "password", "assertion", and "refresh_token". Default
   * description is "authorization_code,refresh_token".
   */
  @Column(name = "authorized_grant_type_str")
  private String authorizedGrantTypeStr;

  /**
   * The redirect URI(s) established during registration (optional, comma separated).
   */
  @Column(name = "registered_redirect_uri_str")
  private String registeredRedirectUriStr;

  /**
   * Authorities that are granted to the client (comma-separated). Distinct from the authorities
   * granted to the user on behalf of whom the client is acting.
   * <pre>
   *     For example: USER
   * </pre>
   */
  @Column(name = "authorities_str")
  private String authoritiesStr;

  /**
   * The access token validity period in seconds (optional). If unspecified a global default will be
   * applied by the token services.
   */
  @Builder.Default
  @Column(name = "access_token_validity_seconds_alias")
  private int accessTokenValiditySecondsAlias = 1800;

  /**
   * The refresh token validity period in seconds (optional). If unspecified a global default will
   * be applied by the token services.
   */
  @Builder.Default
  @Column(name = "refresh_token_validity_seconds_alias")
  private int refreshTokenValiditySecondsAlias = 3600;

  /**
   * Additional information for this client, not needed by the vanilla OAuth protocol but might be
   * useful, for example, for storing descriptive information.
   */
  @Column(name = "additional_information_str")
  private String additionalInformationStr;

  @Column(name = "created_at", nullable = false, updatable = false)
  private long createdAt;

  @Column(name = "created_by", nullable = false, updatable = false)
  private String createdBy;

  @Column(name = "last_modified_at", nullable = false)
  private long lastModifiedAt;

  @Column(name = "last_modified_by", nullable = false)
  private String lastModifiedBy;

  @Version
  @Column(name = "version", nullable = false)
  private int version;

  @Column(name = "public_key", nullable = false)
  private String publicKey;

  public Client(Client client) {
    super();
    this.clientIdAlias = client.getClientIdAlias();
    this.resourceIdStr = client.getResourceIdStr();
    this.clientSecretAlias = client.getClientSecretAlias();
    this.scopeStr = client.getScopeStr();
    this.authorizedGrantTypeStr = client.getAuthorizedGrantTypeStr();
    this.registeredRedirectUriStr = client.getRegisteredRedirectUriStr();
    this.authoritiesStr = client.getAuthoritiesStr();
    this.accessTokenValiditySecondsAlias = client.getAccessTokenValiditySecondsAlias();
    this.refreshTokenValiditySecondsAlias = client.getRefreshTokenValiditySecondsAlias();
    this.additionalInformationStr = client.getAdditionalInformationStr();
  }

  @Override public String getClientId() {
    return getClientIdAlias();
  }

  @Override public Set<String> getResourceIds() {
    return str2Set(getResourceIdStr());
  }

  @Override public boolean isSecretRequired() {
    return true;
  }

  @Override public String getClientSecret() {
    return getClientSecretAlias();
  }

  @Override public boolean isScoped() {
    return true;
  }

  @Override public Set<String> getScope() {
    return str2Set(getScopeStr());
  }

  @Override public Set<String> getAuthorizedGrantTypes() {
    return str2Set(getAuthorizedGrantTypeStr());
  }

  @Override public Set<String> getRegisteredRedirectUri() {
    return str2Set(getRegisteredRedirectUriStr());
  }

  @Override public Collection<GrantedAuthority> getAuthorities() {
    return Arrays.stream(getAuthorizedGrantTypeStr().split(CommonsConstant.COMMA))
        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override public Integer getAccessTokenValiditySeconds() {
    return getAccessTokenValiditySecondsAlias();
  }

  @Override public Integer getRefreshTokenValiditySeconds() {
    return null;
  }

  @Override public boolean isAutoApprove(String scope) {
    return false;
  }

  @Override public Map<String, Object> getAdditionalInformation() {
    return null;
  }

  private Set<String> str2Set(String str) {
    if (StringUtils.isBlank(str)) {
      return new HashSet<>();
    }
    return Sets
        .newHashSet(Arrays.stream(str.split(CommonsConstant.COMMA)).collect(Collectors.toList()));
  }
}
