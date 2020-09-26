/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.filesystem.enums;

/**
 * OSS权限控制枚举.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public enum AccessControlEnum {
    /**
     * 继承Bucket	文件遵循存储空间的访问权限。
     */
    DEFAULT,
    /**
     * 私有	文件的拥有者和授权用户有该文件的读写权限，其他用户没有权限操作该文件。
     */
    PRIVATE,
    /**
     * 公共读	文件的拥有者和授权用户有该文件的读写权限，其他用户只有文件的读权限。请谨慎使用该权限。
     */
    PUBLIC_READ,
    /**
     * 公共读写	所有用户都有该文件的读写权限。请谨慎使用该权限。
     */
    PUBLIC_READ_WRITE
}
