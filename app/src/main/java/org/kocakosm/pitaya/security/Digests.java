/*----------------------------------------------------------------------------*
 * This file is part of Pitaya.                                               *
 * Copyright (C) 2012-2015 Osman KOCAK <kocakosm@gmail.com>                   *
 *                                                                            *
 * This program is free software: you can redistribute it and/or modify it    *
 * under the terms of the GNU Lesser General Public License as published by   *
 * the Free Software Foundation, either version 3 of the License, or (at your *
 * option) any later version.                                                 *
 * This program is distributed in the hope that it will be useful, but        *
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY *
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public     *
 * License for more details.                                                  *
 * You should have received a copy of the GNU Lesser General Public License   *
 * along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *----------------------------------------------------------------------------*/

package org.kocakosm.pitaya.security;

import org.kocakosm.pitaya.util.CannotHappenException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Somme commonly used digest algorithms. None of the {@link Digest} instances
 * returned by this class is thread-safe.
 *
 * @author Osman KOCAK
 */
public final class Digests
{
	/**
	 * Returns a new MD2 {@code Digest} instance.
	 *
	 * @return a new MD2 {@code Digest} instance.
	 */
	public static Digest md2()
	{
		return new MD2();
	}

	/**
	 * Returns a new MD4 {@code Digest} instance.
	 *
	 * @return a new MD4 {@code Digest} instance.
	 */
	public static Digest md4()
	{
		return new MD4();
	}

	/**
	 * Returns a new MD5 {@code Digest} instance.
	 *
	 * @return a new MD5 {@code Digest} instance.
	 */
	public static Digest md5()
	{
		return BuiltInDigest.create("MD5");
	}

	/**
	 * Returns a new SHA1 {@code Digest} instance.
	 *
	 * @return a new SHA1 {@code Digest} instance.
	 */
	public static Digest sha1()
	{
		return BuiltInDigest.create("SHA1");
	}

	/**
	 * Returns a new SHA-256 {@code Digest} instance.
	 *
	 * @return a new SHA-256 {@code Digest} instance.
	 */
	public static Digest sha256()
	{
		return BuiltInDigest.create("SHA-256");
	}

	/**
	 * Returns a new SHA-512 {@code Digest} instance.
	 *
	 * @return a new SHA-512 {@code Digest} instance.
	 */
	public static Digest sha512()
	{
		return BuiltInDigest.create("SHA-512");
	}

	/**
	 * Returns a new Keccak-224 {@code Digest} instance.
	 *
	 * @return a new Keccak-224 {@code Digest} instance.
	 */
	public static Digest keccak224()
	{
		return new Keccak(28);
	}

	/**
	 * Returns a new Keccak-256 {@code Digest} instance.
	 *
	 * @return a new Keccak-256 {@code Digest} instance.
	 */
	public static Digest keccak256()
	{
		return new Keccak(32);
	}

	/**
	 * Returns a new Keccak-384 {@code Digest} instance.
	 *
	 * @return a new Keccak-384 {@code Digest} instance.
	 */
	public static Digest keccak384()
	{
		return new Keccak(48);
	}

	/**
	 * Returns a new Keccak-512 {@code Digest} instance.
	 *
	 * @return a new Keccak-512 {@code Digest} instance.
	 */
	public static Digest keccak512()
	{
		return new Keccak(64);
	}

	private static final class BuiltInDigest extends AbstractDigest
	{
		static Digest create(String algorithm)
		{
			MessageDigest md;
			try {
				md = MessageDigest.getInstance(algorithm);
			} catch (NoSuchAlgorithmException ex) {
				throw new CannotHappenException(ex);
			}
			return new BuiltInDigest(md);
		}

		private final MessageDigest md;

		private BuiltInDigest(MessageDigest md)
		{
			super(md.getAlgorithm(), md.getDigestLength());
			this.md = md;
		}

		@Override
		public Digest reset()
		{
			md.reset();
			return this;
		}

		@Override
		public Digest update(byte input)
		{
			md.update(input);
			return this;
		}

		@Override
		public Digest update(byte... input)
		{
			md.update(input);
			return this;
		}

		@Override
		public Digest update(byte[] input, int off, int len)
		{
			md.update(input, off, len);
			return this;
		}

		@Override
		public byte[] digest()
		{
			return md.digest();
		}

		@Override
		public byte[] digest(byte... input)
		{
			return md.digest(input);
		}

		@Override
		public byte[] digest(byte[] input, int off, int len)
		{
			md.update(input, off, len);
			return md.digest();
		}
	}

	private Digests()
	{
		/* ... */
	}
}
