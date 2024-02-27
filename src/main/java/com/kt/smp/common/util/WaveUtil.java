/**
 * tcs version 1.0
 *
 *  Copyright ⓒ 2017 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *
 * @author kt
 * @since 2018. 6. 29.
 * @version 1.0
 * @see
 * @Copyright © 2017 By KT corp. All rights reserved.
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *  수정일               수정자                수정내용
 *  -------------        ----------       -------------------------
 *  2018. 6. 29.         	 hmook            최초생성
 *
 * </pre>
 **/

package com.kt.smp.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @title WAV 파일 util
 * @since 2022.02.22
 * @author soohyun
 * @see <pre></pre>
 */
public class WaveUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(WaveUtil.class);
	private final static int MAX_BUFFER = 2048;

	public enum ReadType {
		WAVE_FILE_TO_WAVE_BINARY,
		WAVE_FILE_TO_PCM_BINARY,
		PCM_FILE_TO_PCM_BINARY;
	}

	public enum HEADER {

		RIFF_Chunk_ID(0, 4, "RIFF"),
		RIFF_Chuck_Size(4, 4, null),
		RIFF_Format(8, 4, "WAVE"),
		FMT_Chuck_ID(12, 4, "fmt "),
		FMT_Chuck_Size(16, 4, null),
		FMT_Audio_Format(20, 2, null),
		FMT_Number_Of_Channel(22, 2, null),
		FMT_Sample_Rate(24, 4, null),
		FMT_Byte_Rate(28, 4, null),
		FMT_Block_Align(32, 2, null),
		FMT_Bit_Per_Sample(34, 2, null),
		DATA_Chunk_ID(36, 4, "data"),
		DATA_Chuck_Size(40, 4, null),
		;

		private int startIndex;
		private int size;
		private String fixedValue;

		public int getStartIndex() {
			return startIndex;
		}

		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public String getFixedValue() {
			return fixedValue;
		}

		public void setFixedValue(String fixedValue) {
			this.fixedValue = fixedValue;
		}

		HEADER(int startIndex, int size, String fixedValue) {
			this.startIndex = startIndex;
			this.size = size;
			this.fixedValue = fixedValue;
		}
	}

	public static class WavSpecModel {
		public int sampleRate;
		public int bitPerSample;
		public int channelNum;

		public WavSpecModel(int sampleRate, int bitPerSample, int channelNum) {
			this.sampleRate = sampleRate;
			this.bitPerSample = bitPerSample;
			this.channelNum = channelNum;
		}

		@Override
		public String toString() {
			return "WavSpecModel [sampleRate=" + sampleRate + ", bitPerSample=" + bitPerSample + ", channelNum="
				+ channelNum + "]";
		}
	}

	/**
	 * wave 파일 생성
	 */
	public static byte[] makeWave(int channelNum, int sampleRate, int sampleBits, byte[] pcmBytes) {

		byte[] wav = new byte[44 + pcmBytes.length];
		System.arraycopy(makeWaveHeader(pcmBytes.length, channelNum, sampleRate, sampleBits), 0, wav, 0, 44);
		System.arraycopy(pcmBytes, 0, wav, 44, pcmBytes.length);

		return wav;
	}

	/**
	 * wave header 생성
	 */
	public static byte[] makeWaveHeader(int pcmSize, int numChannels, int sampleRate, int bitsPerSample) {

		byte[] wav = new byte[44 + pcmSize];

		// Make Header
		System.arraycopy(HEADER.RIFF_Chunk_ID.fixedValue.getBytes(StandardCharsets.UTF_8), 0, wav,
			HEADER.RIFF_Chunk_ID.startIndex, HEADER.RIFF_Chunk_ID.size);
		System.arraycopy(intToByteArray(pcmSize + 36), 0, wav, HEADER.RIFF_Chuck_Size.startIndex,
			HEADER.DATA_Chuck_Size.size);
		System.arraycopy(HEADER.RIFF_Format.fixedValue.getBytes(StandardCharsets.UTF_8), 0, wav,
			HEADER.RIFF_Format.startIndex, HEADER.RIFF_Format.size);
		System.arraycopy(HEADER.FMT_Chuck_ID.fixedValue.getBytes(StandardCharsets.UTF_8), 0, wav,
			HEADER.FMT_Chuck_ID.startIndex, HEADER.FMT_Chuck_ID.size);
		System.arraycopy(intToByteArray(16), 0, wav, HEADER.FMT_Chuck_Size.startIndex, HEADER.FMT_Chuck_Size.size);
		System.arraycopy(shortToByteArray((short)1), 0, wav, HEADER.FMT_Audio_Format.startIndex,
			HEADER.FMT_Audio_Format.size);
		System.arraycopy(shortToByteArray((short)numChannels), 0, wav, HEADER.FMT_Number_Of_Channel.startIndex,
			HEADER.FMT_Number_Of_Channel.size);
		System.arraycopy(intToByteArray(sampleRate), 0, wav, HEADER.FMT_Sample_Rate.startIndex,
			HEADER.FMT_Sample_Rate.size);
		System.arraycopy(intToByteArray((bitsPerSample / 8) * sampleRate * numChannels), 0, wav,
			HEADER.FMT_Byte_Rate.startIndex, HEADER.FMT_Byte_Rate.size);
		System.arraycopy(shortToByteArray((short)(numChannels * (bitsPerSample / 8))), 0, wav,
			HEADER.FMT_Block_Align.startIndex, HEADER.FMT_Block_Align.size);
		System.arraycopy(shortToByteArray((short)bitsPerSample), 0, wav, HEADER.FMT_Bit_Per_Sample.startIndex,
			HEADER.FMT_Bit_Per_Sample.size);
		System.arraycopy(HEADER.DATA_Chunk_ID.fixedValue.getBytes(StandardCharsets.UTF_8), 0, wav,
			HEADER.DATA_Chunk_ID.startIndex, HEADER.DATA_Chunk_ID.size);
		System.arraycopy(HEADER.DATA_Chunk_ID.fixedValue.getBytes(StandardCharsets.UTF_8), 0, wav,
			HEADER.DATA_Chunk_ID.startIndex, HEADER.DATA_Chunk_ID.size);
		System.arraycopy(intToByteArray(pcmSize), 0, wav, HEADER.DATA_Chuck_Size.startIndex,
			HEADER.DATA_Chuck_Size.size);

		return wav;
	}

	/**
	 * 오디오 파일을 읽어 유형에 따라 binary data 반환
	 */
	public static byte[] getBytes(File file, ReadType readType) {

		switch (readType) {
			case PCM_FILE_TO_PCM_BINARY:
				return pcmFileToPcmBinary(file);

			case WAVE_FILE_TO_PCM_BINARY:
				return waveFileToPcmBinary(file);

			case WAVE_FILE_TO_WAVE_BINARY:
				return waveFileToWaveBinary(file);

			default:
				return null;
		}
	}

	/**
	 * WAV의 헤더 정보 중 일부를 조회
	 */
	public static byte[] getWaveHeader(byte[] wav, HEADER header) {
		byte[] bytes = new byte[header.getSize()];

		for (int i = 0; i < header.getSize(); i++) {
			bytes[0] = wav[header.getStartIndex() + i];
		}

		return bytes;
	}

	/**
	 * WAV의 헤더 정보 중 Channel 수를 조회
	 */
	public static int getWaveNumOfChannel(byte[] wav) {
		byte[] bytes = new byte[HEADER.FMT_Number_Of_Channel.getSize()];

		for (int i = 0; i < HEADER.FMT_Number_Of_Channel.getSize(); i++) {
			bytes[i] = wav[HEADER.FMT_Number_Of_Channel.getStartIndex() + i];
		}

		return byteToInt(bytes);
	}

	/**
	 * WAV의 헤더 정보 중 SampleRate를 조회
	 */
	public static int getWaveSampleRate(byte[] wav) {
		byte[] bytes = new byte[HEADER.FMT_Sample_Rate.getSize()];

		for (int i = 0; i < HEADER.FMT_Sample_Rate.getSize(); i++) {
			bytes[i] = wav[HEADER.FMT_Sample_Rate.getStartIndex() + i];
		}

		return byteToInt(bytes);
	}

	/**
	 * WAV의 헤더 정보 중 BitPerSample를 조회
	 */
	public static int getWaveBitPerSample(byte[] wav) {
		byte[] bytes = new byte[HEADER.FMT_Bit_Per_Sample.getSize()];

		for (int i = 0; i < HEADER.FMT_Bit_Per_Sample.getSize(); i++) {
			bytes[i] = wav[HEADER.FMT_Bit_Per_Sample.getStartIndex() + i];
		}

		return byteToInt(bytes);
	}

	/**
	 * WAV에서 Header를 제외한 PCM데이터만을 반환한다
	 */
	public static byte[] getWavePCMData(byte[] wav) {
		byte[] pcmData = new byte[wav.length - 44];
		System.arraycopy(wav, 44, pcmData, 0, wav.length - 44);

		return pcmData;
	}

	/**
	 * 묵음 PCM 데이터를 생성하여 반환
	 */
	public static byte[] getSilentWav(int sampleRate, int numChannels, int bitsPerSample, int millisecond) {

		int pcmSize = (sampleRate * numChannels * bitsPerSample) / 8 * millisecond / 1000;

		byte[] wav = new byte[44 + pcmSize];
		byte[] pcmData = new byte[pcmSize];
		for (int i = 0; i < pcmSize; i++) {
			pcmData[i] = 0;
		}

		// Make Header
		System.arraycopy(makeWaveHeader(pcmSize, numChannels, sampleRate, bitsPerSample), 0, wav, 0, 44);

		// Add PCM Data
		System.arraycopy(pcmData, 0, wav, 44, pcmData.length);
		pcmData = null;

		return wav;
	}

	/**
	 * int 를 byte[] 로 변환 : little endian 방식
	 */
	private static byte[] intToByteArray(int i) {
		byte[] b = new byte[4];
		b[0] = (byte)(i & 0x00FF);
		b[1] = (byte)((i >> 8) & 0x000000FF);
		b[2] = (byte)((i >> 16) & 0x000000FF);
		b[3] = (byte)((i >> 24) & 0x000000FF);

		return b;
	}

	/**
	 * short 를 byte[] 로 변환 : little endian 방식
	 */
	public static byte[] shortToByteArray(short data) {

		return new byte[] {(byte)(data & 0xff), (byte)((data >> 8) & 0xff)};
	}

	/**
	 * little endian bytes를 int로 변환
	 */
	public static int littleEndianBytesToInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < bytes.length; i++) {
			if (i == 0) {
				result |= bytes[i] << (8 * i);
			} else {
				result |= bytes[i] << (8 * i);
			}
		}
		return result;
	}

	/**
	 * little endian byte array를 short or int로 변환
	 */
	public static int byteToInt(byte[] arr) {

		if (arr.length == 2) {
			return 0x00 |
				0x00 |
				(arr[1] & 0xff) << 8 |
				(arr[0] & 0xff);
		} else if (arr.length == 4) {
			return (arr[3] & 0xff) << 24 |
				(arr[2] & 0xff) << 16 |
				(arr[1] & 0xff) << 8 |
				(arr[0] & 0xff);
		} else if (arr.length == 8) {
			return (arr[7] & 0xff) << 56 |
				(arr[6] & 0xff) << 48 |
				(arr[5] & 0xff) << 40 |
				(arr[4] & 0xff) << 32 |
				(arr[3] & 0xff) << 24 |
				(arr[2] & 0xff) << 16 |
				(arr[1] & 0xff) << 8 |
				(arr[0] & 0xff);
		} else {
			return 0;
		}

	}

	/**
	 * PCM의 사이즈를 반환
	 *
	 * @param sampleRate
	 * @param numChannels
	 * @param bitsPerSample
	 * @param millisecond
	 */
	public static int getPcmLength(int sampleRate, int numChannels, int bitsPerSample, int millisecond) {
		return (sampleRate * numChannels * bitsPerSample) / 8 * millisecond / 1000;
	}

	/**
	 * 재생시간(ms)을 반환하는 함수
	 *
	 * @param pcmSize
	 * @param sampleRate
	 * @param channelNum
	 * @param bitsPerSample
	 */
	public static long getPcmPlayTime(int pcmSize, int sampleRate, int channelNum, int bitsPerSample) {
		return (long)((float)pcmSize / sampleRate / channelNum / bitsPerSample * 8 * 1000);
	}

	/**
	 * PCM 파일을 읽어 PCM 바이너리 데이터를 반환
	 */
	public static byte[] pcmFileToPcmBinary(File file) {

		byte[] data = null;

		try (InputStream channelInput = Files.newInputStream(file.toPath());
			 ByteArrayOutputStream byteOutput = new ByteArrayOutputStream()) {

			byte[] buffer = new byte[MAX_BUFFER];
			int c = 0;
			while ((c = channelInput.read(buffer, 0, buffer.length)) != -1) {
				byteOutput.write(buffer, 0, c);
			}

			data = byteOutput.toByteArray();

		} catch (IOException e) {
			LOGGER.error("pcm file to pcm binary Exception {}", e.getMessage());
		}

		return data;
	}

	/**
	 * WAV 파일을 읽어 PCM 바이너리 데이터를 반환
	 */
	public static byte[] waveFileToPcmBinary(File file) {

		byte[] data = null;

		try (AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);
			 ByteArrayOutputStream byteOutput = new ByteArrayOutputStream()) {

			byte[] buffer = new byte[MAX_BUFFER];
			int c = 0;
			while ((c = audioInput.read(buffer, 0, buffer.length)) != -1) {
				byteOutput.write(buffer, 0, c);
			}

			data = byteOutput.toByteArray();

		} catch (IOException | UnsupportedAudioFileException e) {
			LOGGER.error("wav file to pcm binary Exception {}", e.getMessage());
		}

		return data;
	}

	/**
	 * WAV 파일을 읽어 WAV 바이너리 데이터를 반환
	 */
	public static byte[] waveFileToWaveBinary(File file) {
		byte[] data = null;

		try (AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);
			 ByteArrayOutputStream byteOutput = new ByteArrayOutputStream()) {

			AudioSystem.write(audioInput, AudioFileFormat.Type.WAVE, byteOutput);

			data = byteOutput.toByteArray();

		} catch (IOException | UnsupportedAudioFileException e) {
			LOGGER.error("wav file to wav binary Exception {}", e.getMessage());
		}

		return data;
	}

	/**
	 * WAV 바이너리를 읽어 PCM 바이너리 데이터로 반환 (헤더 제외)
	 */
	public static byte[] waveBinaryToPcmBinary(byte[] wavBinary) {

		byte[] data = null;

		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(wavBinary);
			 AudioInputStream audioInput = AudioSystem.getAudioInputStream(inputStream);
			 ByteArrayOutputStream byteOutput = new ByteArrayOutputStream()) {

			byte[] buffer = new byte[MAX_BUFFER];
			int c = 0;
			while ((c = audioInput.read(buffer, 0, buffer.length)) != -1) {
				byteOutput.write(buffer, 0, c);
			}

			data = byteOutput.toByteArray();

		} catch (IOException | UnsupportedAudioFileException e) {
			LOGGER.error("wav file to pcm binary Exception {}", e.getMessage());
		}

		return data;
	}

	/**
	 * 입력받은 WAV를 표준 WAV 헤더(44byte)로 교체 후 반환
	 */
	public static byte[] convertToStandardWavData(byte[] originBytes, WavSpecModel spec) {

		return WaveUtil.makeWave(spec.channelNum,
			spec.sampleRate,
			spec.bitPerSample,
			WaveUtil.waveBinaryToPcmBinary(originBytes));
	}

	/**
	 * 입력받은 WAV의 품질정보를 파싱하여 반환
	 */
	public static WavSpecModel parseWavSpec(final byte[] originBytes) {

		if (originBytes == null) {
			LOGGER.error("(!) parseWavSpec:: origin wav bytes is null");
			return null;
		}

		int channelNum = -1;
		float sampleRate = -1;
		int bitPerSample = -1;

		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(originBytes);
			 AudioInputStream audioInput = AudioSystem.getAudioInputStream(inputStream)) {

			if (inputStream != null) {
				channelNum = audioInput.getFormat().getChannels();
				sampleRate = audioInput.getFormat().getSampleRate();
				bitPerSample = audioInput.getFormat().getSampleSizeInBits();
			}

		} catch (UnsupportedAudioFileException | IOException e) {
			LOGGER.error("(!) parseWavSpec Exception {}", e.getMessage());
		}

		return new WavSpecModel((int)sampleRate, bitPerSample, channelNum);
	}

	/**
	 * 입력받은 WAV의 SampleRate를 변환하여 반환
	 */
	public static byte[] convertWavSampleRate(final byte[] originBytes, int sampleRate) {

		if (originBytes == null) {
			LOGGER.error("convertWavSampleRate:: origin wav bytes is null");
			return null;
		}

		byte[] wav = null;
		byte[] buffer = null;
		ByteArrayInputStream inputStream = null;
		AudioInputStream ais = null;
		AudioFormat audioFormat = null;
		AudioInputStream newAis = null;
		ByteArrayOutputStream baos = null;

		try {
			inputStream = new ByteArrayInputStream(originBytes);
			if (inputStream != null) {
				ais = AudioSystem.getAudioInputStream(inputStream);

				int originChannel = ais.getFormat().getChannels();
				float originSampleRate = ais.getFormat().getSampleRate();
				boolean isBigEndian = ais.getFormat().isBigEndian();
				int originSamplePerBit = ais.getFormat().getSampleSizeInBits();

				if (originSampleRate != sampleRate) {
					audioFormat = new AudioFormat(Encoding.PCM_SIGNED, (float)sampleRate, originSamplePerBit,
						originChannel, originChannel * 2, originSampleRate, isBigEndian);

					if (audioFormat != null) {
						newAis = AudioSystem.getAudioInputStream(audioFormat, ais);
						if (newAis != null) {
							baos = new ByteArrayOutputStream();
							buffer = new byte[MAX_BUFFER];
							int readSize = 0;
							while ((readSize = newAis.read(buffer)) > 0) {
								baos.write(buffer, 0, readSize);
							}
							baos.flush();

							wav = WaveUtil.makeWave(originChannel, sampleRate, originSamplePerBit, baos.toByteArray());
						}
					}
				} else {
					wav = originBytes;
				}
			}
		} catch (UnsupportedAudioFileException | IOException e) {
			LOGGER.error("convertWavSampleRate Exception {}", e.getMessage());
		} finally {
			if (ais != null) {
				try {
					ais.close();
				} catch (IOException e) {
					LOGGER.error("convertWavSampleRate Exception {}", e.getMessage());
				}
				ais = null;
			}

			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					LOGGER.error("convertWavSampleRate Exception {}", e.getMessage());
				}
				baos = null;
			}

			if (newAis != null) {
				try {
					newAis.close();
				} catch (IOException e) {
					LOGGER.error("convertWavSampleRate Exception {}", e.getMessage());
				}
				newAis = null;
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOGGER.error("convertWavSampleRate Exception {}", e.getMessage());
				}
				inputStream = null;
			}
			audioFormat = null;
			buffer = null;
		}

		return wav;
	}
}
