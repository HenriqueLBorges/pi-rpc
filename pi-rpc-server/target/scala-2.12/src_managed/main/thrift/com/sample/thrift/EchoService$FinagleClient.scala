/**
 * Generated by Scrooge
 *   version: 19.5.0
 *   rev: e4dcc42ba660bdf59f3049d98cca03a3e043a348
 *   built at: 20190517-122041
 */
package com.sample.thrift

import com.twitter.finagle.{service => ctfs}
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}
import com.twitter.finagle.thrift.{Protocols, RichClientParam, ThriftClientRequest}
import com.twitter.util.Future
import org.apache.thrift.TApplicationException
import org.apache.thrift.protocol._


@javax.annotation.Generated(value = Array("com.twitter.scrooge.Compiler"))
class EchoService$FinagleClient(
    val service: com.twitter.finagle.Service[ThriftClientRequest, Array[Byte]],
    val clientParam: RichClientParam)
  extends EchoService[Future] {

  @deprecated("Use com.twitter.finagle.thrift.RichClientParam", "2017-08-16")
  def this(
    service: com.twitter.finagle.Service[ThriftClientRequest, Array[Byte]],
    protocolFactory: TProtocolFactory = Protocols.binaryFactory(),
    serviceName: String = "EchoService",
    stats: StatsReceiver = NullStatsReceiver,
    responseClassifier: ctfs.ResponseClassifier = ctfs.ResponseClassifier.Default
  ) = this(
    service,
    RichClientParam(
      protocolFactory,
      serviceName,
      clientStats = stats,
      responseClassifier = responseClassifier
    )
  )

  @deprecated("Use com.twitter.finagle.thrift.RichClientParam", "2017-08-16")
  def this(
    service: com.twitter.finagle.Service[ThriftClientRequest, Array[Byte]],
    protocolFactory: TProtocolFactory,
    serviceName: String,
    stats: StatsReceiver
  ) = this(service, protocolFactory, serviceName, stats, ctfs.ResponseClassifier.Default)

  import EchoService._

  def serviceName: String = clientParam.serviceName

  override def asClosable: _root_.com.twitter.util.Closable = service

  private[this] def protocolFactory: TProtocolFactory = clientParam.restrictedProtocolFactory
  private[this] def maxReusableBufferSize: Int = clientParam.maxThriftBufferSize

  private[this] val tlReusableBuffer: _root_.com.twitter.scrooge.TReusableBuffer =
    _root_.com.twitter.scrooge.TReusableBuffer(maxThriftBufferSize = maxReusableBufferSize)

  protected def encodeRequest(name: String, args: _root_.com.twitter.scrooge.ThriftStruct): ThriftClientRequest = {
    val memoryBuffer = tlReusableBuffer.get()
    try {
      val oprot = protocolFactory.getProtocol(memoryBuffer)

      oprot.writeMessageBegin(new TMessage(name, TMessageType.CALL, 0))
      args.write(oprot)
      oprot.writeMessageEnd()
      oprot.getTransport().flush()
      val bytes = _root_.java.util.Arrays.copyOfRange(
        memoryBuffer.getArray(),
        0,
        memoryBuffer.length()
      )
      new ThriftClientRequest(bytes, false)
    } finally {
      tlReusableBuffer.reset()
    }
  }

  protected def decodeResponse[T <: _root_.com.twitter.scrooge.ThriftStruct](
    resBytes: Array[Byte],
    codec: _root_.com.twitter.scrooge.ThriftStructCodec[T]
  ): T = {
    val iprot = protocolFactory.getProtocol(
      new org.apache.thrift.transport.TMemoryInputTransport(resBytes)
    )
    val msg = iprot.readMessageBegin()
    try {
      if (msg.`type` == TMessageType.EXCEPTION) {
        val exception = TApplicationException.readFrom(iprot) match {
          case sourced: _root_.com.twitter.finagle.SourcedException =>
            if (serviceName != "") sourced.serviceName = serviceName
            sourced
          case e => e
        }
        throw exception
      } else {
        codec.decode(iprot)
      }
    } finally {
      iprot.readMessageEnd()
    }
  }

  protected def missingResult(name: String): TApplicationException = {
    new TApplicationException(
      TApplicationException.MISSING_RESULT,
      name + " failed: unknown result"
    )
  }

  protected def setServiceName(ex: Throwable): Throwable =
    if (this.serviceName == "") ex
    else {
      ex match {
        case se: _root_.com.twitter.finagle.SourcedException =>
          se.serviceName = this.serviceName
          se
        case _ => ex
      }
    }

  // ----- end boilerplate.

  private[this] def stats: StatsReceiver = clientParam.clientStats
  private[this] def responseClassifier: ctfs.ResponseClassifier = clientParam.responseClassifier

  private[this] val scopedStats: StatsReceiver = if (serviceName != "") stats.scope(serviceName) else stats
  private[this] object __stats_ping {
    val RequestsCounter = scopedStats.scope("ping").counter("requests")
    val SuccessCounter = scopedStats.scope("ping").counter("success")
    val FailuresCounter = scopedStats.scope("ping").counter("failures")
    val FailuresScope = scopedStats.scope("ping").scope("failures")
  }
  val pingEchoServiceReplyDeserializer: Array[Byte] => _root_.com.twitter.util.Try[com.sample.thrift.Pong] = {
    response: Array[Byte] => {
      val result = decodeResponse(response, Ping.Result)
  
      result.firstException() match {
        case Some(exception) => _root_.com.twitter.util.Throw(setServiceName(exception))
        case _ => result.successField match {
          case Some(success) => _root_.com.twitter.util.Return(success)
          case _ => _root_.com.twitter.util.Throw(missingResult("ping"))
        }
      }
    }
  }
  
  def ping(name: String): Future[com.sample.thrift.Pong] = {
    __stats_ping.RequestsCounter.incr()
    val inputArgs = Ping.Args(name)
  
    val serdeCtx = new _root_.com.twitter.finagle.thrift.ClientDeserializeCtx[com.sample.thrift.Pong](inputArgs, pingEchoServiceReplyDeserializer)
    _root_.com.twitter.finagle.context.Contexts.local.let(
      _root_.com.twitter.finagle.thrift.ClientDeserializeCtx.Key,
      serdeCtx,
      _root_.com.twitter.finagle.thrift.Headers.Request.Key,
      _root_.com.twitter.finagle.thrift.Headers.Request.newValues
    ) {
      serdeCtx.rpcName("ping")
      val start = System.nanoTime
      val serialized = encodeRequest("ping", inputArgs)
      serdeCtx.serializationTime(System.nanoTime - start)
      this.service(serialized).flatMap { response =>
        Future.const(serdeCtx.deserialize(response))
      }.respond { response =>
        val responseClass = responseClassifier.applyOrElse(
          ctfs.ReqRep(inputArgs, response),
          ctfs.ResponseClassifier.Default)
        responseClass match {
          case ctfs.ResponseClass.Successful(_) =>
            __stats_ping.SuccessCounter.incr()
          case ctfs.ResponseClass.Failed(_) =>
            __stats_ping.FailuresCounter.incr()
            response match {
              case _root_.com.twitter.util.Throw(ex) =>
                setServiceName(ex)
                __stats_ping.FailuresScope.counter(
                  _root_.com.twitter.util.Throwables.mkString(ex): _*).incr()
              case _ =>
            }
        }
      }
    }
  }
  private[this] object __stats_tell {
    val RequestsCounter = scopedStats.scope("tell").counter("requests")
    val SuccessCounter = scopedStats.scope("tell").counter("success")
    val FailuresCounter = scopedStats.scope("tell").counter("failures")
    val FailuresScope = scopedStats.scope("tell").scope("failures")
  }
  val tellEchoServiceReplyDeserializer: Array[Byte] => _root_.com.twitter.util.Try[String] = {
    response: Array[Byte] => {
      val result = decodeResponse(response, Tell.Result)
  
      result.firstException() match {
        case Some(exception) => _root_.com.twitter.util.Throw(setServiceName(exception))
        case _ => result.successField match {
          case Some(success) => _root_.com.twitter.util.Return(success)
          case _ => _root_.com.twitter.util.Throw(missingResult("tell"))
        }
      }
    }
  }
  
  def tell(name: String): Future[String] = {
    __stats_tell.RequestsCounter.incr()
    val inputArgs = Tell.Args(name)
  
    val serdeCtx = new _root_.com.twitter.finagle.thrift.ClientDeserializeCtx[String](inputArgs, tellEchoServiceReplyDeserializer)
    _root_.com.twitter.finagle.context.Contexts.local.let(
      _root_.com.twitter.finagle.thrift.ClientDeserializeCtx.Key,
      serdeCtx,
      _root_.com.twitter.finagle.thrift.Headers.Request.Key,
      _root_.com.twitter.finagle.thrift.Headers.Request.newValues
    ) {
      serdeCtx.rpcName("tell")
      val start = System.nanoTime
      val serialized = encodeRequest("tell", inputArgs)
      serdeCtx.serializationTime(System.nanoTime - start)
      this.service(serialized).flatMap { response =>
        Future.const(serdeCtx.deserialize(response))
      }.respond { response =>
        val responseClass = responseClassifier.applyOrElse(
          ctfs.ReqRep(inputArgs, response),
          ctfs.ResponseClassifier.Default)
        responseClass match {
          case ctfs.ResponseClass.Successful(_) =>
            __stats_tell.SuccessCounter.incr()
          case ctfs.ResponseClass.Failed(_) =>
            __stats_tell.FailuresCounter.incr()
            response match {
              case _root_.com.twitter.util.Throw(ex) =>
                setServiceName(ex)
                __stats_tell.FailuresScope.counter(
                  _root_.com.twitter.util.Throwables.mkString(ex): _*).incr()
              case _ =>
            }
        }
      }
    }
  }
}