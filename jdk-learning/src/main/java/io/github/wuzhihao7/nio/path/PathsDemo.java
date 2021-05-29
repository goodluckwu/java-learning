package io.github.wuzhihao7.nio.path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathsDemo {
    private static final Logger log = LoggerFactory.getLogger(PathsDemo.class);
    public static void main(String[] args) throws URISyntaxException, IOException {
        Path path = Path.of("c:/test.txt");
        log.info("{}", path);

        path = Path.of("c:\\dev\\test.txt");
        log.info("{}", path);

        path = Path.of("c:", "dev", "test.txt");
        log.info("{}", path);

        path = Path.of("c:", "dev").resolve("test.txt");
        log.info("{}", path);

        path = Path.of(new URI("file:///c:/dev/test.txt"));
        log.info("{}", path);

        path = Paths.get("c:/", "dev", "test.txt");
        log.info("{}", path);

        /**
         * 判断此路径是否绝对。
         * 绝对路径是完整的，因为它不需要与其他路径信息组合以便定位文件。
         */
        log.info("{}", path.isAbsolute());
        log.info("{}", Path.of("dev/test.txt").isAbsolute());
        log.info("{}", path.getFileSystem());

        /**
         * 返回此路径的根组分作为 Path对象，或 null如果该路径不具有根组件。
         */
        log.info("{}", path.getRoot());
        log.info("{}", Path.of("dev/test.txt").getRoot());
        log.info("{}", Path.of("test.txt").getRoot());
        log.info("{}", Path.of("/").getRoot());

        /**
         * 返回此路径表示为Path对象的文件或目录的名称。 文件名是目录层次结构中根目录的最远元素。
         */
        log.info("{}", path.getFileName());
        log.info("{}", Path.of("dev/test.txt").getFileName());
        log.info("{}", Path.of("test.txt").getFileName());
        log.info("{}", Path.of("").getFileName());
        log.info("{}", Path.of("/").getFileName());
        log.info("{}", Path.of("c:/").getFileName());
        log.info("{}", Path.of("c:/dev").getFileName());

        /**
         * 返回父路径 ，如果此路径没有父路径 ，则返回null 。
         */
        log.info("{}", path.getParent());
        log.info("{}", Path.of("dev/test.txt").getParent());
        log.info("{}", Path.of("test.txt").getParent());
        log.info("{}", Path.of("").getParent());
        log.info("{}", Path.of("/").getParent());
        log.info("{}", Path.of("c:/").getParent());
        log.info("{}", Path.of("c:/dev").getParent());

        /**
         * 返回路径中的名称元素数。路径中的元素数，如果此路径仅表示根组件， 0
         */
        log.info("{}", Path.of("c:/0/1/2/t.txt").getNameCount());
        log.info("{}", Path.of("dev/test.txt").getNameCount());
        log.info("{}", Path.of("test.txt").getNameCount());
        log.info("{}", Path.of("").getNameCount());
        log.info("{}", Path.of("/").getNameCount());
        log.info("{}", Path.of("c:/").getNameCount());
        log.info("{}", Path.of("c:/dev").getNameCount());

        /**
         * 以Path对象的形式返回此路径的名称元素。
         */
        log.info("{}", Path.of("c:/0/1/2/t.txt").getName(0));
        log.info("{}", Path.of("c:/0/1/2/t.txt").getName(Path.of("c:/0/1/2/t.txt").getNameCount()-1));

        /**
         * 返回一个相对Path ，它是此路径的name元素的子序列。
         * beginIndex和endIndex参数指定名称元素的子序列。 最接近目录层次结构中的根的名称具有索引0 。 距离根最远的名称的索引为count -1 。 返回的Path对象具有名称元素，该元素从beginIndex开始并扩展到索引endIndex-1处的元素。
         */
        log.info("{}", Path.of("c:/0/1/2/t.txt").subpath(0, path.getNameCount()));
        log.info("{}", Path.of("c:/0/1/2/t.txt").subpath(0, 1));

        /**
         * 返回此路径的路径，其中删除了冗余名称元素。
         * 此方法的精确定义取决于实现，但通常它源自此路径，即不包含冗余名称元素的路径。 在许多文件系统中，“ . ”和“ .. ”是用于指示当前目录和父目录的特殊名称。 在这种文件系统中，所有出现的“ . ”都被认为是多余的。 如果“ .. ”之前是非“ .. ”名称，则这两个名称都被视为冗余（识别此类名称的过程将重复，直至不再适用）。
         *
         * 此方法不访问文件系统; 路径可能找不到存在的文件。 从路径中.. “ .. ”和前一个名称可能会导致找到与原始路径不同的文件的路径。 当前面的名称是符号链接时，可能会出现这种情况。
         */
        log.info("{}", Path.of("c:/a/b/.././test.txt").normalize());
        log.info("{}", Path.of("c:/a/b/./../test.txt").normalize());

        /**
         * 根据此路径解析给定路径。
         * 如果other参数是absolute路径，则此方法other返回other 。 如果other是空路径，则此方法other会返回此路径。 否则，此方法将此路径视为目录，并针对此路径解析给定路径。 在最简单的情况下，给定的路径不具有root部件，在这种情况下该方法加入给定的路径向此路径，并返回所得到的路径ends与给定的路径。 在给定路径具有根组件的情况下，解决方案高度依赖于实现，因此未指定。
         *
         */
        log.info("{}", Path.of("c:/aa").resolve("test.txt"));
        log.info("{}", Path.of("c:/aa").resolve("c:/bb/test.txt"));
        log.info("{}", Path.of("c:/aa").resolve(""));
        log.info("{}", Path.of("c:/aa").resolve("bb/test.txt"));
        log.info("{}", Path.of("c:/aa/test.txt").resolve("bb/test.txt"));

        /**
         * 根据此路径的路径解析给定路径parent 。 在需要用其他文件名替换文件名的情况下，这很有用。 例如，假设名称分隔符为“ / ”，路径表示“ dir1/dir2/foo ”，则使用Path “ bar ”调用此方法将生成Path “ dir1/dir2/bar ”。 如果此路径没有父路径，或other是absolute ，则此方法返回other 。 如果other是空路径，则此方法返回此路径的父路径，或者此路径没有父路径，即空路径。
         */
        log.info("{}", Path.of("c:/aa.txt").resolveSibling("bb.txt"));
        log.info("{}", Path.of("c:/aa.txt").resolveSibling("c:/bb.txt"));
        log.info("{}", Path.of("c:/aa.txt").resolveSibling(""));
        log.info("{}", Path.of("c:/").resolveSibling(""));
        log.info("{}", Path.of("c:/").resolveSibling("a.txt"));
        log.info("{}", Path.of("").resolveSibling("bb.txt"));

        /**
         * 构造此路径与给定路径之间的相对路径。
         * 相对论是resolution的逆。 此方法尝试构造一个relative路径，该路径在resolved针对此路径时，生成一个路径，该路径定位与给定路径相同的文件。 例如，在UNIX上，如果此路径为"/a/b"且给定路径为"/a/b/c/d"则生成的相对路径为"c/d" 。 如果此路径和给定路径没有root组件，则可以构造相对路径。 如果只有一个路径具有根组件，则无法构建相对路径。 如果两个路径都具有根组件，那么如果可以构造相对路径，则它是依赖于实现的。 如果此路径和给定路径为equal，则返回空路径 。
         *
         * 对于任何两个normalized路径p和q ，其中q没有根组件，
         *
         * p .relativize( p .resolve( q )).equals( q )
         * 当支持符号链接时，那么当针对此路径解析时，生成的路径是否会产生可用于定位same文件的路径，因为other是依赖于实现的。 例如，如果此路径为"/a/b"且给定路径为"/a/x"则生成的相对路径可能为"../x" 。 如果"b"是符号链接，则依赖于实现，如果"a/b/../x"将找到与"/a/x"相同的文件。
         */
        log.info("{}", Path.of("c:/a/b/c/d/a.txt").relativize(Path.of("c:/")));
        log.info("{}", Path.of("c:/").relativize(Path.of("c:/a/b/c/d/a.txt")));

        /**
         * 返回表示此路径的绝对路径的Path对象。
         * 如果此路径已经是absolute，则此方法只返回此路径。 否则，此方法通常通过针对文件系统缺省目录解析路径，以依赖于实现的方式解析路径。 根据实现，如果文件系统不可访问，此方法可能会抛出I / O错误。
         */
        log.info("{}", Path.of("c:/a/b/test.txt").toAbsolutePath());
        log.info("{}", Path.of("a/b/test.txt").toAbsolutePath());

        log.info("{}", Path.of("c:/aa/test.txt").toRealPath());
    }
}

