package com.bytedance.android.netview.library;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author yulu
 */
public class SlowParser implements XmlResourceParser {

    private final SlowAttributeSet mSlowAttributeSet;
    private final XmlPullParser mParser;

    public SlowParser(Resources res, XmlPullParser parser) {
        mParser = parser;
        mSlowAttributeSet = new SlowAttributeSet(res, mParser);
    }

    @Override
    public void close() {
    }

    @Override
    public int getAttributeNameResource(int index) {
        return mSlowAttributeSet.getAttributeNameResource(index);
    }

    @Override
    public int getAttributeListValue(String namespace, String attribute, String[] options, int defaultValue) {
        return mSlowAttributeSet.getAttributeListValue(namespace, attribute, options, defaultValue);
    }

    @Override
    public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue) {
        return mSlowAttributeSet.getAttributeBooleanValue(namespace, attribute, defaultValue);
    }

    @Override
    public int getAttributeResourceValue(String namespace, String attribute, int defaultValue) {
        return mSlowAttributeSet.getAttributeResourceValue(namespace, attribute, defaultValue);
    }

    @Override
    public int getAttributeIntValue(String namespace, String attribute, int defaultValue) {
        return mSlowAttributeSet.getAttributeIntValue(namespace, attribute, defaultValue);
    }

    @Override
    public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue) {
        return mSlowAttributeSet.getAttributeUnsignedIntValue(namespace, attribute, defaultValue);
    }

    @Override
    public float getAttributeFloatValue(String namespace, String attribute, float defaultValue) {
        return mSlowAttributeSet.getAttributeFloatValue(namespace, attribute, defaultValue);
    }

    @Override
    public int getAttributeListValue(int index, String[] options, int defaultValue) {
        return mSlowAttributeSet.getAttributeListValue(index, options, defaultValue);
    }

    @Override
    public boolean getAttributeBooleanValue(int index, boolean defaultValue) {
        return mSlowAttributeSet.getAttributeBooleanValue(index, defaultValue);
    }

    @Override
    public int getAttributeResourceValue(int index, int defaultValue) {
        return mSlowAttributeSet.getAttributeResourceValue(index, defaultValue);
    }

    @Override
    public int getAttributeIntValue(int index, int defaultValue) {
        return mSlowAttributeSet.getAttributeIntValue(index, defaultValue);
    }

    @Override
    public int getAttributeUnsignedIntValue(int index, int defaultValue) {
        return mSlowAttributeSet.getAttributeUnsignedIntValue(index, defaultValue);
    }

    @Override
    public float getAttributeFloatValue(int index, float defaultValue) {
        return mSlowAttributeSet.getAttributeFloatValue(index, defaultValue);
    }

    @Override
    public String getIdAttribute() {
        return mSlowAttributeSet.getIdAttribute();
    }

    @Override
    public String getClassAttribute() {
        return mSlowAttributeSet.getClassAttribute();
    }

    @Override
    public int getIdAttributeResourceValue(int defaultValue) {
        return mSlowAttributeSet.getIdAttributeResourceValue(defaultValue);
    }

    @Override
    public int getStyleAttribute() {
        return mSlowAttributeSet.getStyleAttribute();
    }

    @Override
    public void setFeature(String name, boolean state) throws XmlPullParserException {
        mParser.setFeature(name, state);
    }

    @Override
    public boolean getFeature(String name) {
        return mParser.getFeature(name);
    }

    @Override
    public void setProperty(String name, Object value) throws XmlPullParserException {
        mParser.setProperty(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return mParser.getProperty(name);
    }

    @Override
    public void setInput(Reader in) throws XmlPullParserException {
        mParser.setInput(in);
    }

    @Override
    public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {
        mParser.setInput(inputStream, inputEncoding);
    }

    @Override
    public String getInputEncoding() {
        return mParser.getInputEncoding();
    }

    @Override
    public void defineEntityReplacementText(String entityName, String replacementText) throws XmlPullParserException {
        mParser.defineEntityReplacementText(entityName, replacementText);
    }

    @Override
    public int getNamespaceCount(int depth) throws XmlPullParserException {
        return mParser.getNamespaceCount(depth);
    }

    @Override
    public String getNamespacePrefix(int pos) throws XmlPullParserException {
        return mParser.getNamespacePrefix(pos);
    }

    @Override
    public String getNamespaceUri(int pos) throws XmlPullParserException {
        return mParser.getNamespaceUri(pos);
    }

    @Override
    public String getNamespace(String prefix) {
        return mParser.getNamespace(prefix);
    }

    @Override
    public int getDepth() {
        return mParser.getDepth();
    }

    @Override
    public String getPositionDescription() {
        return mParser.getPositionDescription();
    }

    @Override
    public int getLineNumber() {
        return mParser.getLineNumber();
    }

    @Override
    public int getColumnNumber() {
        return mParser.getColumnNumber();
    }

    @Override
    public boolean isWhitespace() throws XmlPullParserException {
        return mParser.isWhitespace();
    }

    @Override
    public String getText() {
        return mParser.getText();
    }

    @Override
    public char[] getTextCharacters(int[] holderForStartAndLength) {
        return mParser.getTextCharacters(holderForStartAndLength);
    }

    @Override
    public String getNamespace() {
        return mParser.getNamespace();
    }

    @Override
    public String getName() {
        return mParser.getName();
    }

    @Override
    public String getPrefix() {
        return mParser.getPrefix();
    }

    @Override
    public boolean isEmptyElementTag() throws XmlPullParserException {
        return mParser.isEmptyElementTag();
    }

    @Override
    public int getAttributeCount() {
        return mSlowAttributeSet.getAttributeCount();
    }

    @Override
    public String getAttributeNamespace(int index) {
        return mSlowAttributeSet.getAttributeName(index);
    }

    @Override
    public String getAttributeName(int index) {
        return mSlowAttributeSet.getAttributeName(index);
    }

    @Override
    public String getAttributePrefix(int index) {
        return mParser.getAttributePrefix(index);
    }

    @Override
    public String getAttributeType(int index) {
        return mParser.getAttributeType(index);
    }

    @Override
    public boolean isAttributeDefault(int index) {
        return mParser.isAttributeDefault(index);
    }

    @Override
    public String getAttributeValue(int index) {
        return mSlowAttributeSet.getAttributeValue(index);
    }

    @Override
    public String getAttributeValue(String namespace, String name) {
        return mSlowAttributeSet.getAttributeValue(namespace, name);
    }

    @Override
    public int getEventType() throws XmlPullParserException {
        return mParser.getEventType();
    }

    @Override
    public int next() throws XmlPullParserException, IOException {
        return mParser.next();
    }

    @Override
    public int nextToken() throws XmlPullParserException, IOException {
        return mParser.nextToken();
    }

    @Override
    public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {
        mParser.require(type, namespace, name);
    }

    @Override
    public String nextText() throws XmlPullParserException, IOException {
        return mParser.nextText();
    }

    @Override
    public int nextTag() throws XmlPullParserException, IOException {
        return mParser.nextTag();
    }
}
