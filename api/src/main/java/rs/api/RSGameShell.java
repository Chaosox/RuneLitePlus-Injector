/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package rs.api;

import java.awt.Canvas;

import api.GameShell;
import net.runelite.mapping.Import;

public interface RSGameShell extends GameShell
{
	@Import("canvas")
	Canvas getCanvas();

	@Import("post")
	void post(Object canvas);

	@Import("resizeCanvas")
	@Override
	void resizeCanvas();

	@Import("resizeCanvasNextFrame")
	boolean isResizeCanvasNextFrame();

	@Import("resizeCanvasNextFrame")
	void setResizeCanvasNextFrame(boolean resize);

	@Import("replaceCanvasNextFrame")
	boolean isReplaceCanvasNextFrame();

	@Import("replaceCanvasNextFrame")
	void setReplaceCanvasNextFrame(boolean replace);

	@Import("maxCanvasWidth")
	void setMaxCanvasWidth(int width);

	@Import("maxCanvasHeight")
	void setMaxCanvasHeight(int height);
}
