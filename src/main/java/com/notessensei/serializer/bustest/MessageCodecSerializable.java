package com.notessensei.serializer.bustest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class MessageCodecSerializable<T> implements MessageCodec<T, T> {

  @Override
  public void encodeToWire(Buffer buffer, T s) {
    if (!(s instanceof Serializable)) {
      throw new RuntimeException(s.getClass().getName() + " doesn't implement Serializable");
    }

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos)) {
      out.writeObject(s);
      out.close();
      buffer.appendBytes(baos.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println("encodeToWire");
  }

  @SuppressWarnings("unchecked")
  @Override
  public T decodeFromWire(int pos, Buffer buffer) {

    T result = null;

    final int length = buffer.getInt(pos);
    final int start = pos + 4; // int was 4 byte
    final int end = pos + 4 + length;

    ByteArrayInputStream bais = new ByteArrayInputStream(buffer.getBytes(start, end));

    try (ObjectInputStream in = new ObjectInputStream(bais)) {
      result = (T) in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    System.out.println("decodeFromWire");
    return result;
  }

  @Override
  public T transform(T s) {
    return s;
  }

  @Override
  public String name() {
    return this.getClass().getName();
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }

}
