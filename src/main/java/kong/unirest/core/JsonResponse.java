/**
 * The MIT License
 *
 * Copyright for portions of unirest-java are held by Kong Inc (c) 2013.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package kong.unirest.core;

import java.util.Objects;

public class JsonResponse extends BaseResponse<JsonNode> {
    private JsonNode node;

    protected JsonResponse(RawResponse response) {
        super(response);
        node = getNode(response);
    }

    private JsonNode getNode(RawResponse response) {
        if (Objects.isNull(response) || !response.hasContent()) {
            return new JsonNode(null);
        } else {
            String json = response.getContentAsString();
            return toJsonNode(json);
        }
    }

    private JsonNode toJsonNode(String json) {
        try {
            return new JsonNode(json);
        } catch (RuntimeException e) {
            super.setParsingException(json, e);
            return null;
        }
    }

    @Override
    public JsonNode getBody() {
        return node;
    }

    @Override
    protected String getRawBody() {
        return node.toString();
    }
}
