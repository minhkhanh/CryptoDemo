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

import org.kocakosm.pitaya.util.BigEndian;
import org.kocakosm.pitaya.util.ByteBuffer;
import org.kocakosm.pitaya.util.Parameters;
import org.kocakosm.pitaya.util.XObjects;

/**
 * PBKDF2 Key Derivation Function (RFC 2898). Instances of this class are
 * immutable.
 *
 * @author Osman KOCAK
 */
final class PBKDF2 implements KDF
{
	private final int dkLen;
	private final int iterationCount;
	private final Algorithm<MAC> algorithm;

	/**
	 * Creates a new {@code PBKDF2} instance.
	 *
	 * @param algorithm the MAC algorithm to use.
	 * @param iterationCount the desired number of iterations.
	 * @param dkLen the desired length for derived keys, in bytes.
	 *
	 * @throws NullPointerException if {@code algorithm} is {@code null}.
	 * @throws IllegalArgumentException if {@code iterationCount} or
	 *	{@code dkLen} is negative, or if the MAC algorithm is unknown.
	 */
	PBKDF2(Algorithm<MAC> algorithm, int iterationCount, int dkLen)
	{
		Parameters.checkCondition(dkLen > 0);
		Parameters.checkCondition(iterationCount > 0);
		Factory.newMAC(algorithm, new byte[0]);
		this.algorithm = algorithm;
		this.iterationCount = iterationCount;
		this.dkLen = dkLen;
	}

	@Override
	public byte[] deriveKey(byte[] secret, byte[] salt)
	{
		MAC mac = Factory.newMAC(algorithm, secret);
		int d = (int) Math.ceil((double) dkLen / mac.length());
		ByteBuffer t = new ByteBuffer(d * mac.length());
		for (int i = 1; i <= d; i++) {
			byte[] f = mac.update(salt).digest(BigEndian.encode(i));
			byte[] u = f;
			for (int j = 1; j < iterationCount; j++) {
				u = mac.digest(u);
				for (int k = 0; k < f.length; k++) {
					f[k] ^= u[k];
				}
			}
			t.append(f);
		}
		return t.toByteArray(0, dkLen);
	}

	@Override
	public String toString()
	{
		return XObjects.toStringBuilder("PBKDF2")
			.append("MAC", algorithm)
			.append("iterationCount", iterationCount)
			.append("dkLen", dkLen).toString();
	}
}
