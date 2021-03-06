/*
 * Copyright 2016 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vertx.mqtt;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.impl.Arguments;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.KeyCertOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.core.net.TrustOptions;

/**
 * Represents options used by the MQTT server
 */
@DataObject
public class MqttServerOptions extends NetServerOptions {

  public static final int DEFAULT_PORT = 1883; // Default port is 1883 for MQTT
  public static final int DEFAULT_TLS_PORT = 8883; // Default TLS port is 8883 for MQTT

  public static final int DEFAULT_MAX_MESSAGE_SIZE = -1;
  public static final int DEFAULT_TIMEOUT_ON_CONNECT = 90;

  // max message size (variable header + payload) in bytes
  private int maxMessageSize;
  // if clientid should be autogenerated (if "zero-bytes")
  private boolean isAutoClientId;
  // timeout on CONNECT packet
  private int timeoutOnConnect;

  /**
   * Default constructor
   */
  public MqttServerOptions() {
    super();
    // override the default port
    this.setPort(DEFAULT_PORT);
    this.maxMessageSize = DEFAULT_MAX_MESSAGE_SIZE;
    this.isAutoClientId = true;
    this.timeoutOnConnect = DEFAULT_TIMEOUT_ON_CONNECT;
  }

  /**
   * Create an options from JSON
   *
   * @param json the JSON
   */
  public MqttServerOptions(JsonObject json) {
    super(json);
    // override the default port
    this.setPort(json.getInteger("port", DEFAULT_PORT));
    this.maxMessageSize =  json.getInteger("maxMessageSize", DEFAULT_MAX_MESSAGE_SIZE);
    this.isAutoClientId = json.getBoolean("isAutoClientId", true);
    this.timeoutOnConnect = json.getInteger("timeoutOnConnect", DEFAULT_TIMEOUT_ON_CONNECT);

    if ((this.maxMessageSize > 0) && (this.getReceiveBufferSize() > 0)) {
      Arguments.require(this.getReceiveBufferSize() >= this.maxMessageSize,
        "Receiver buffer size can't be lower than max message size");
    }
  }

  /**
   * Copy constructor
   *
   * @param other the options to copy
   */
  public MqttServerOptions(MqttServerOptions other) {
    super(other);
  }

  @Override
  public MqttServerOptions setPort(int port) {
    super.setPort(port);
    return this;
  }

  @Override
  public MqttServerOptions setHost(String host) {
    super.setHost(host);
    return this;
  }

  @Override
  public MqttServerOptions setClientAuth(ClientAuth clientAuth) {
    super.setClientAuth(clientAuth);
    return this;
  }

  @Override
  public MqttServerOptions setSsl(boolean ssl) {
    super.setSsl(ssl);
    return this;
  }

  @Override
  public MqttServerOptions setKeyCertOptions(KeyCertOptions options) {
    super.setKeyCertOptions(options);
    return this;
  }

  @Override
  public MqttServerOptions setKeyStoreOptions(JksOptions options) {
    super.setKeyStoreOptions(options);
    return this;
  }

  @Override
  public MqttServerOptions setPfxKeyCertOptions(PfxOptions options) {
    super.setPfxKeyCertOptions(options);
    return this;
  }

  @Override
  public MqttServerOptions setPemKeyCertOptions(PemKeyCertOptions options) {
    super.setPemKeyCertOptions(options);
    return this;
  }

  @Override
  public MqttServerOptions setTrustOptions(TrustOptions options) {
    super.setTrustOptions(options);
    return this;
  }

  @Override
  public MqttServerOptions setTrustStoreOptions(JksOptions options) {
    super.setTrustStoreOptions(options);
    return this;
  }

  @Override
  public MqttServerOptions setPemTrustOptions(PemTrustOptions options) {
    super.setPemTrustOptions(options);
    return this;
  }

  @Override
  public MqttServerOptions setPfxTrustOptions(PfxOptions options) {
    super.setPfxTrustOptions(options);
    return this;
  }

  @Override
  public MqttServerOptions addEnabledCipherSuite(String suite) {
    super.addEnabledCipherSuite(suite);
    return this;
  }

  @Override
  public MqttServerOptions addEnabledSecureTransportProtocol(final String protocol) {
    super.addEnabledSecureTransportProtocol(protocol);
    return this;
  }

  @Override
  public MqttServerOptions addCrlPath(String crlPath) throws NullPointerException {
    super.addCrlPath(crlPath);
    return this;
  }

  @Override
  public MqttServerOptions addCrlValue(Buffer crlValue) throws NullPointerException {
    super.addCrlValue(crlValue);
    return this;
  }

  @Override
  public MqttServerOptions setReceiveBufferSize(int receiveBufferSize) {
    if ((this.maxMessageSize > 0) && (receiveBufferSize > 0)) {
      Arguments.require(receiveBufferSize >= this.maxMessageSize,
        "Receiver buffer size can't be lower than max message size");
    }
    super.setReceiveBufferSize(receiveBufferSize);
    return this;
  }

  @Override
  public MqttServerOptions setSni(boolean sni) {
    super.setSni(sni);
    return this;
  }

  /**
   * Set max MQTT message size
   *
   * @param maxMessageSize  max MQTT message size (variable header + payload)
   * @return  MQTT server options instance
   */
  public MqttServerOptions setMaxMessageSize(int maxMessageSize) {
    Arguments.require(maxMessageSize > 0 || maxMessageSize == DEFAULT_MAX_MESSAGE_SIZE, "maxMessageSize must be > 0");
    if ((maxMessageSize > 0) && (this.getReceiveBufferSize() > 0)) {
      Arguments.require(this.getReceiveBufferSize() >= maxMessageSize,
        "Receiver buffer size can't be lower than max message size");
    }
    this.maxMessageSize = maxMessageSize;
    return this;
  }

  /**
   * @return  max MQTT message size (variable header + payload)
   */
  public int getMaxMessageSize() {
    return this.maxMessageSize;
  }

  /**
   * Set if clientid should be auto-generated when it's "zero-bytes"
   *
   * @param isAutoClientId
   * @return  MQTT server options instance
   */
  public MqttServerOptions setAutoClientId(boolean isAutoClientId) {
    this.isAutoClientId = isAutoClientId;
    return this;
  }

  /**
   * @return  if clientid should be auto-generated when it's "zero-bytes" (default is true)
   */
  public boolean isAutoClientId() {
    return this.isAutoClientId;
  }

  /**
   * Set the timeout on CONNECT packet
   *
   * @param timeoutOnConnect timeout on CONNECT before closing connection
   * @return  MQTT server options instance
   */
  public MqttServerOptions setTimeoutOnConnect(int timeoutOnConnect) {
    this.timeoutOnConnect = timeoutOnConnect;
    return this;
  }

  /**
   * @return  timeout on CONNECT before closing connection
   */
  public int timeoutOnConnect() {
    return this.timeoutOnConnect;
  }
}
